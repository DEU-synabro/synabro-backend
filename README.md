# Synabro Server
## Contents
- [Instroduction](#Introduction)
- [Requirements](#Requriements)
- [Installation](#Installation)
- [Deployment](#Deployment)
- [Style Guide](#Style-Guide)
- [Contributing](#Contributing)
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

## Deployment
### Configuration
- Configurate Docker environment file.
    - Copy `.env.example` file and set the file name `.env`
    - Enter the mariaDB information
        ```
        MARIADB_ROOT_PASSWORD={root_password}
        MARIADB_DATABASE={database_name}
        MARIADB_USER={user_name}
        MARIADB_PASSWORD={user_password}
        ```
    - Example
        ```
        MARIADB_ROOT_PASSWORD=rootPassword
        MARIADB_DATABASE=testDatabase
        MARIADB_USER=testUsername
        MARIADB_PASSWORD=testPassword
        ```
- Edit application-prod.yml file
    - Correct the part below
        ```
        spring:
            datasource:
                driver-class-name: org.mariadb.jdbc.Driver
                url: jdbc:mariadb://localhost:3306/{database_name}
                username: {user_name}
                password: {user_password}
            ...
        ```
    - Example
        ```
        spring:
            datasource:
                driver-class-name: org.mariadb.jdbc.Driver
                url: jdbc:mariadb://localhost:3306/testDatabase
                username: testUsername
                password: testPassword
            ...
        ```
  - If you want to use another profile option, you have to copy `application-prod.yml` file and change name to `application-{profile name}.yml`

### Run Docker container for mariaDB
```
docker-compose up
```

### Use HAL Browser
After building the project, access `localhost:port/browser/index.html`

## Style Guide
- I referred to [Google Style Guidelines](https://github.com/JunHoPark93/google-java-styleguide)
- Source file structure
    - A source file consists of, **in order**:
        - License or copyright information, if present
        - Package statement
        - Import statements
        - Exactly one top-level class
- Class Name
    - Use PascalCase
    - Example
        - `public class HelloWorld {}`
- Method Name
    - Use lowerCamelCase
    - Begin with a verb/preposition
    - Example
        - `public void getUserByName(){}`
        - `public void toString(){}`
- Variable Name
    - Use lowerCamelCase
    - Example
        - `private String myName`

## Contributing
1. Create issues about the work.
2. Create a branch on the issue.
3. Commit, push to the created branch.
4. When the work is completed, request a pull request to main branch after rebaseing the main branch.
5. Review the code and merge it.

### Branching
```
ISSUE_NUMBER-description
```
- e.g. Issue 2 related to user authentication.
    ```
    2-user-authentication
    ```

### Commit Message
Referred to [Beom Dev Log](https://beomseok95.tistory.com/328) and [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)

```
<type>[optional scope]: <description>
[optional body]
[optional footer(s)]
```
- Type
    - build, docs, feat, fix, perf, reactor, test
- Example
    ```
    feat: allow provided config object to extend other configs
    ```

## Maintainers
Current maintainers:
- Jiyoon Bak - https://github.com/jiy00nn