#!/usr/bin/env bash

set -o errexit
set -o pipefail

readonly PROG_NAME=$(basename "$0")
readonly ARGS=("$@")

print_usage() {
    cat <<-EOF

    usage: ${PROG_NAME} parameters options

    Build and optionaly push aio (All In One) and confd docker images


    OPTIONS:
       -h   print this help
       -c   build the 'confd' images
       -a   build the aio 'images'
       -p   push to docker hub
       -v   specify the tag version (default will be 'latest')
       -n   specify the image namespace (default 'looztra')
       -l   specify the components to build (among 'gateway', 'filter', 'storage', 'fontend')
                use the comma (',') as a separator
                unknown components will be ignored
                if not specified, all components will be built

    Examples:
       ${PROG_NAME} -c -a -p -v 0.6.0
EOF
}

showoff() {
    echo "========================================"
    echo "build_confd              = ${build_confd}"
    echo "build_aio                = ${build_aio}"
    echo "push_to_registry         = ${push_to_registry}"
    echo "version                  = ${version}"
    echo "component_list_as_string = ${component_list_as_string}"
    echo "========================================"
}

get_image_name() {
    local name
    local type

    name=$1
    type=$2

    echo "${namespace}/${name}:${version}-${type}"
}

build_image() {
    local directory
    local name
    local type
    local image_name

    directory=$1
    name=$2
    type=$3
    image_name=$(get_image_name $name $type)

    echo "== Building image [${image_name}] from directory [${directory}]"
    docker build -t ${image_name} -f "${directory}/Dockerfile.${type}" "${directory}"
    all_built_images=(${all_built_images[@]} ${image_name})
    echo
}

build_type() {
    local type
    type=$1
    echo "== Building ${type} images =="

    for component in "${components[@]}"; do
        #    echo "component: ${component}, directory: ${componentToDirectory[$component]}"
        if [ ! -z ${componentToDirectory[$component]} ]; then
            build_image "${componentToDirectory[$component]}" "guestbook-${component}" "${type}"
        else
            echo "Ignoring unknown component [${component}]"
        fi
    done
}

build_aio() {
    build_type "aio"
}

build_confd() {
    build_type "confd"
}

report() {

    local opt_push_message=" (nothing pushed)"
    if [[ "${push_to_registry}" == "true" ]]; then
        opt_push_message=" and pushed"
    fi
    echo "== Report of all images built${opt_push_message} =="
    for img in "${all_built_images[@]}"; do
        echo "${img}"
    done
    echo "===================================================="
    echo
}

push_to_registry() {
    echo "== Pushing images to registry =="
    for img in "${all_built_images[@]}"; do
        docker push "${img}"
    done
    echo "================================"
    echo
}

build_component_list() {
    IFS=',' read -r -a components <<<"${component_list_as_string}"
}

init_components_metadata() {
    componentToDirectory["filter"]="guestbook-filter-service"
    componentToDirectory["frontend"]="guestbook-simple-frontend"
    componentToDirectory["gateway"]="guestbook-gateway-service"
    componentToDirectory["storage"]="guestbook-storage-service"

    component_list_as_string="filter,storage,gateway,frontend"
}

main() {
    init_components_metadata
    showoff
    build_component_list
    if [[ "${build_aio}" == "true" ]]; then
        build_aio
    fi
    if [[ "${build_confd}" == "true" ]]; then
        build_confd
    fi
    if [[ "${push_to_registry}" == "true" ]]; then
        push_to_registry
    fi
    report

}

#
# Default values for params
#
version=latest
push_to_registry=false
build_aio=false
build_confd=false
namespace=looztra
#
# script global vars
#
component_list_as_string=
declare -a all_built_images=()
declare -A componentToDirectory
declare -a components

while getopts "pcav:n:l:h" opt; do
    case ${opt} in
        c)
            build_confd=true
            ;;
        a)
            build_aio=true
            ;;
        p)
            push_to_registry=true
            ;;
        v)
            version="$OPTARG"
            ;;
        n)
            namespace="$OPTARG"
            ;;
        l)
            component_list_as_string="$OPTARG"
            ;;
        h)
            print_usage
            exit 0
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            print_usage
            exit 1
            ;;
    esac
done
shift $((OPTIND - 1))

main
