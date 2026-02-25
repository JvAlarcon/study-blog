FROM clojure:openjdk-17-lein
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN lein deps
CMD ["lein", "ring", "server"]