for dir in back-end/*; do
  if [ -f "$dir/pom.xml" ]; then
    echo "=== Running tests in $dir ==="
    mvn -f "$dir/pom.xml" test || { echo "Tests failed in $dir"; exit 1; }
  fi
done
echo "All module tests finished."