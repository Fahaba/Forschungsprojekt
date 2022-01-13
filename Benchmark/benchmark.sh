#! /bin/bash

child_min=1
depth_min=1
child_max=2
depth_max=2

IFS=':'

while getopts ":c:d:v:" opt; do
  case $opt in
  c)
    child=$OPTARG
    if [[ "$child" =~ ":" ]]; then
      read -ra ARR <<<"$child"
      child_min=${ARR[0]}
      child_max=${ARR[1]}
    else
      child_max=$child
    fi
    ;;
  d)
    DEPTH=$OPTARG
    if [[ "$DEPTH" =~ ":" ]]; then
      read -ra ARR <<<"$DEPTH"
      depth_min=${ARR[0]}
      depth_max=${ARR[1]}
    else
      depth_max=$DEPTH
    fi
    ;;
  \?)
    echo "Invalid option: -$OPTARG" >&2
    exit 1
    ;;
  :)
    echo "Option -$OPTARG requires an argument." >&2
    exit 1
    ;;
  esac
done

IFS=' '

echo -e "Children: $child_min...$child_max"
echo -e "Depth:    $depth_min...$depth_max\n"

current_dir=$(pwd)
output_dir="$current_dir/out"
module_dir="$output_dir/modules"
csv_dir="$output_dir/data"
csv_memory="c$child_min-${child_max}_d$depth_min-${depth_max}_mem.csv"
csv_time="c$child_min-${child_max}_d$depth_min-${depth_max}_tim.csv"

echo "Output directory:        $output_dir"
echo "Data directory:          $csv_dir"
echo "Gradle project location: $module_dir"
echo "Memory file:             $csv_memory"
echo "Time file:               $csv_time"

csv_memory="$csv_dir/$csv_memory"
csv_time="$csv_dir/$csv_time"

# if [[ ! -d $output_dir ]]; then
#   mkdir $output_dir
# fi
# if [[ ! -d $csv_dir ]]; then
#   mkdir $csv_dir
# fi
# # if [[ ! -d $module_dir ]]; then
# #   mkdir $module_dir
# # fi

# if [[ -f "$csv_memory" ]]; then
#   echo "Found old memory file"
#   echo "Removing old $csv_memory file..."
#   rm $csv_memory
# fi

# if [[ -f "$csv_time" ]]; then
#   echo "Found old time file"
#   echo "Remove old $csv_time file..."
#   rm $csv_time
# fi


echo -e "\nStart benchmarking\n"

./utils/shell/bench-main.sh $child_min $child_max $depth_min $depth_max $csv_memory $csv_time $module_dir

echo -e "\nBenchmarking done"
echo -e "\nVisualizing results\n"
python3 utils/python/plot.py --out_dir "$output_dir/data" --children $child_min:$child_max --depth $depth_min:$depth_max
# python3 utils/python/plot.py --out_dir "out/data" --children 1:5 --depth 1:5
