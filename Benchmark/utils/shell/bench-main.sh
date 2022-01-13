#! /bin/bash

# $1 = children
# $2 = children
# $3 = depth
# $4 = depth
# $5 = memory csv file
# $6 = time csv file
# $7 = root dir for generated modules

min_child=$1
max_child=$2
min_depth=$3
max_depth=$4

csv_memory=$5
csv_time=$6
gradle_project=$7

out_dir="out/data"
mem_csv="c$min_child-${max_child}_d$min_depth-${max_depth}_mem.csv"
tim_csv="c$min_child-${max_child}_d$min_depth-${max_depth}_tim.csv"
cyc_csv="c$min_child-${max_child}_d$min_depth-${max_depth}_cyc.csv"

rm -rf $out_dir
mkdir -p $out_dir

out_tim="/dev/null"
out_mem="/dev/null"

echo "starting benchmark"


# --- begin stamping methods
  # $1 = children
  # $2 = depth
  # $3 = type (r=run, b=build)
  # $4 = command to execute

memory_stamp() {
  memory=$(free | grep -oP "[\s]+[0-9]+" | grep -oP "[0-9]+" | sed -n 2p)
  time=$(date "+%s:%N")
  echo -e "$time\t$memory\t$1\t$2\t$3" >> $csv_memory
  echo -e "$time\t$memory\t$1\t$2\t$3" >> $out_mem
}

time_stamp() {
  time_start=$(date "+%s:%N")
  perf stat -e cycles -o tmp $4
  time_end=$(date "+%s:%N")

  cycles=`cat tmp`

  if [[ $cycles =~ ([0-9]+),[0-9]+(,[0-9]+)* ]]; then
    cycles=${BASH_REMATCH[0]}
  fi

  echo -e "$1\t$2\t$cycles" >> $out_cyc

  echo -e "$1\t$2\t$time_start\t$time_end\t$3" >> $csv_time
  echo -e "$1\t$2\t$time_start\t$time_end\t$3" >> $out_tim
  memory_stamp $1 $2 $3
}
# --- end stamp methods ---


# --- begin bench methods
# $1 children
# $2 depth
# $3 reserved if needed

bench_run() {
  command="gradle -p $gradle_project-$1$2 :module1:run"
  time_stamp $1 $2 r "$command"
}

bench_build() {
  command="gradle build -p $gradle_project-$1$2"
  time_stamp $1 $2 b "$command"
}

bench_build_javac() {
  command="./utils/shell/manual-build-execute.sh $gradle_project-$1$2 $3"
  time_stamp $1 $2 b "$command"
}

bench_run_java() {
  command="./utils/shell/manual-build-execute.sh $gradle_project-$1$2 $3"
  time_stamp $1 $2 b "$command"
}
# --- end bench methods ---


groovy_build() {
  groovy utils/groovy/Generator.groovy $1 $2 $gradle_project-$1$2 2> /dev/null
}

run_loop() {
  out_mem="$out_dir/$2_$mem_csv"
  out_tim="$out_dir/$2_$tim_csv"
  out_cyc="$out_dir/$2_$cyc_csv"
  ./utils/shell/memory-monitor.sh $out_mem &
  mem1_pid=$!

  for c in $(eval echo {$min_child..$max_child}); do
    for d in $(eval echo {$min_depth..$max_depth}); do
      echo -e "\nrunning loop: $c $d"
      echo "command: $1"
      $1 $c $d $3
    done
  done

  kill $mem1_pid
}

run_loop "groovy_build"
sleep 60

echo "Starting memory monitoring... "
./utils/shell/memory-monitor.sh $csv_memory &
# remember pid to kill it afterwards
mem_pid=$!

run_loop "bench_build_javac"        "jb" 1
run_loop "bench_run_java"           "jr" 2
run_loop "bench_build_javac"        "sb" 3
run_loop "bench_run_java"           "sr" 4
run_loop "bench_build"              "gb"
run_loop "bench_run"                "gr"

kill $mem_pid
