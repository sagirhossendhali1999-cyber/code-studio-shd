OP="com.shd.codestudio"
NP="com.shd.codestudio"
OP_PATH="com.shd.codestudio"
NP_PATH="com.shd.codestudio"
find . -type f -not -path "./.git/*" -not -path "*/build/*" -exec sed -i "s/$OP/$NP/g" {} \;
find . -type f -path "*$OP_PATH*" -not -path "./.git/*" | while read f; do
  nf=$(echo "$f" | sed "s|$OP_PATH|$NP_PATH|g")
  mkdir -p "$(dirname "$nf")"
  mv "$f" "$nf"
done
