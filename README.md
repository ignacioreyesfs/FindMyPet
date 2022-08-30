# FindMyPet

## Description

FindMyPet is a web application developed in Spring Boot and Thymeleaf for posting lost pets.

The motivation for this project is the use of Spring Boot and it's different layers such as spring core, mvc, data access, security and test.

## Features

- Posts
  - Create a post provinding information such as pet's age, breed, photos and the date / location of loss.
  - Update and deletion.
  - Search filters.
  - Notifications requiring updates.

- User
  - Create, update and delete an account.
  - Email validation.
  - Password reset.
  - Admin panel (roles).

## Some screenshots
<div float="left">
<img
  src="https://drive.google.com/uc?export=view&id=1Kgr0N7wbDmRUhfNL08EKQUAISfRyZmnG"
  alt="Posts image"
  style="width: 500px";>
<img
  src="https://drive.google.com/uc?export=view&id=16lGR1M9tcbDaBep3fY-MqBNw_Y3wpVQ-"
  alt="Profile image"
  style="width: 500px";>
</div>

## How do I run this?

### Requirements

To run this application you need the following:
- [Docker](https://www.docker.com/) (with compose) installed on your machine.
- Gmail account and its [application password](https://support.google.com/mail/answer/185833?hl=es-419)

### Steps

1. Clone (or download and unzip) this repo.
2. Application's email account needs to be configured to send emails to users (account validation, updates, etc). To achieve this, please modify docker-compose.yml file by setting EMAIL_USERNAME and EMAIL_PASSWORD environment variables with your email account and the generated application password. You could not set it but email features will not work.
3. Run one of the following command depending on your docker version:

```sh
docker-compose up
```
```sh
docker compose up
```
4. Wait until the application starts and that's it, you can visit [localhost](http://www.localhost:8080/) and use FindMyPet.

### Use
When the application starts, you will find some data already loaded such as posts, users, breeds, locations, etc.

These are the pre-loaded accounts you can log in to:
User | Password | Roles 
--- | --- | ---
exampleadmin1 | Exampleadmin1 | USER, ADMIN
example1 | Example1 | USER
example2 | Example2 | USER
example3 | Example3 | USER
example4 | Example4 | USER
