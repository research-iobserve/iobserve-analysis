#!/bin/sh

repo_path="https://github.com/research-iobserve/iobserve-repository.git"

clone_and_add_path() {
  old_path="$(pwd -P )"
  echo "Path to analysis "$old_path
  git clone $repo_path
  path="$(pwd -P )"
  path=$path"/iobserve-repository/mvn-repo/"	
  echo "Repo at "$path
  cd $old_path
  file_entry="api.baseline="$path
  echo $file_entry >"gradle.properties"
  echo "added/overwrote file 'gradle.properties' and added repo path" 	
}

clone_and_add_path
