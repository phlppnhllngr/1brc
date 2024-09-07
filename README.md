# About

[The One Billion Row Challenge](https://github.com/gunnarmorling/1brc?tab=readme-ov-file#1%EF%B8%8F%E2%83%A3%EF%B8%8F-the-one-billion-row-challenge)

<img src="1brc.png" alt="1BRC" style="display: block; width: 50%;">

# Run code
```sh
./create_measurements.sh 1000000000
./mvnw clean package -DskipTests
time ./calculate_average_baseline.sh
export JFR="false"
./test.sh phlppnhllngr
export JFR="true"
time ./calculate_average_phlppnhllngr.sh
./convert_jfr_to_flamegraph.sh
```

# Paths to explore
- Custom optimized implementation of `java.util.Map`
- `java.nio.channels.FileChannel` and `java.nio.MappedByteBuffer`
- `sun.misc.Unsafe`
- AppCDS, CRaC, Graal native-image
