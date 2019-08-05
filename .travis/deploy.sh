#!/bin/bash

if [[ "${BRANCH}" == 'master' && "${TRAVIS_PULL_REQUEST}" == 'false' ]] || [[ -n "${TRAVIS_TAG}" ]]; then
  .travis/publish-docker.sh
fi