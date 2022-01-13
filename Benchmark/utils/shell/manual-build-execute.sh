#! /bin/bash

run() {
  java --module-path $1/modules -m module1/module1.Module1
}

single_compile() {
  rm -rf $1/modules
  mkdir $1/modules
  javac -Xlint:-module -d $1/modules --module-source-path $1 $(find $1 -name "*.java")
}

compile_loop() {
  MODULE_COUNT=`ls -l -d $1/*/ | grep -c ^d`

  rm -rf $1/modules
  mkdir $1/modules

  for count in $(eval echo {$MODULE_COUNT..1}); do
    javac -Xlint:-module \
      -d $1/modules/module${count} --module-path $1/modules \
      $1/module${count}/src/main/java/module-info.java \
      $1/module${count}/src/main/java/module${count}/Module${count}.java
  done
}

directory=$1
if [[ $2 == 1 ]]; then
  compile_loop $directory
elif [[ $2 == 2 ]]; then
  run $directory
elif [[ $2 == 3 ]]; then
  single_compile "$directory-java"
elif [[ $2 == 4 ]]; then
  run "$directory-java"
else
  echo "ERRRRRRRRRRRRRRRROOOOOOOOOOOOOOOORRRRRRRRRRRRR!"
fi