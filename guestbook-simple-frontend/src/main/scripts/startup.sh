#!/bin/ash

DICT_LOCATION=/config/dictionaries/env.sh
if [ -e "${DICT_LOCATION}" ]; then
    echo "Sourcing ${DICT_LOCATION}"
    # shellcheck source=/dev/null
    source "${DICT_LOCATION}"
else
    echo "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
    echo "No '${DICT_LOCATION}' found, you've been warned!"
    echo "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
fi

if /usr/local/bin/confd -onetime -backend env; then
    echo
    echo "w00t w00t!! config has been generated, let's start nginx now"
    nginx -g 'daemon off;'
else
    echo "You failed at starting confd and/or nginx properly"
fi
