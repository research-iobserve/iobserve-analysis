git clone https://github.com/research-iobserve/iobserve-repository.git
path="$(pwd -P )"
path=$path"/iobserve-repository/mvn-repo/"
echo "Repo at "$path
cd $old_path
file_entry="api.baseline="$path
echo $file_entry >"gradle.properties"
