FROM clojure:openjdk-17-lein

ARG DATOMIC_USER
ARG DATOMIC_PASSWORD

#RUN apt-get update && apt-get install -y \
#    libxrender1 libxtst6 libxi6 \
#    libfreetype6 fontconfig && \
#    rm -rf /var/liba/apt/lists/*

RUN mkdir -p /root/.lein && echo \
    "{:user {:mirrors {\"my.datomic.com\" {:url \"https://my.datomic.com/repo\" \
    :username \"${DATOMIC_USER}\" :password \"${DATOMIC_PASSWORD}\"}}}}}" \
    > /root/.lein/profiles.clj

WORKDIR /usr/src/app
COPY . /usr/src/app
#RUN lein deps
CMD ["lein", "ring", "server"]