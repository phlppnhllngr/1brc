#!/bin/sh
# https://github.com/brendangregg/FlameGraph/issues/242#issuecomment-779263734
java -cp "$HOME"/.m2/repository/tools/profiler/async-profiler-converter/3.0/async-profiler-converter-3.0.jar jfr2flame flight-record.jfr flamegraph.html