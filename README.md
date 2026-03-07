# Study Blog

This is a simple blog aimed to be a case study of an integration with Datomic. It's written in Clojure, and uses Datomic with PostgreSQL as storage. Utilises docker and docker-compose as the infrastructure of the blog.

Keep in mind that to replicate this blog on your machine, it's necessary to have a Datomic licence because that licence will be configured in the `transactor.properties` file, and this template is available at the root of this repository. 

On top of that, it's necessary that the Datomic Pro distribution in `.zip` format is present in the root directory of this repository on your local machine, as specified in the `Dockerfile.transactor` file. 

Until the present moment, I couldn't make the `transactor.properties` file receive all the data configured in the `.env` file, so you need to edit both `.env` with the credentials you want and the URL of Datomic with the same credentials in the `transactor.properties` file.

## Functionalities

Until the present moment, the main functionalities of this blog are the following:
* Article registration
* Article reading
* Article editing
* Article exclusion

## Roadmap

Nevertheless, I want to add the following features to the blog:
* [ ] Store the blog's administrative login data within Datomic;
* [ ] Allow a crud of authors of articles, and create a link with articles and admin permission;
* [ ] Add an access counter to articles;
* [ ] List for admins the most-read articles.