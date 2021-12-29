# Synabro Server
## Contents
- [Instroduction](#Introduction)
- [Requirements](#Requirements)
- [Recommended modules](#Recommended-modules)
- [Installation](#Installation)
- [Configuration](#Configuration)
- [Maintainers](#Maintainers)

## Introduction
The final goal of this project is to provide an online volunteer activity platform that can perform word typing service for the visually impaired and hearing impaired.

## Requriements
- [JDK 1.8](https://www.azul.com/downloads/?version=java-8-lts&package=jdk) or later
- Gradle 4+
- You can also import the code straight into your IDE:
    - [Spring Tool Suite (STS)](https://spring.io/tools)
    - [IntelliJ IDEA](https://www.jetbrains.com/)

## Installation
- Install as you woud normally install a Java JDK 8
- If you use IntelliJ, you must install lombok plugin
    - Click *File > Settings > Plugin* or *Ctrl+Alt+S > Plugin*
    - Search **lombok** and Install

## Configuration
- Configuration Docker environmentfile.
    - Copy `.env.example` file and set the file name `.env`
    - Enter the mariaDB information
        ```
        MARIADB_ROOT_PASSWORD={root_password}
        MARIADB_DATABASE={database_name}
        MARIADB_USER={user_name}
        MARIADB_PASSWORD={user_password}
        ```
- Edit application.yml file
    

## Maintainers