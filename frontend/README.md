# Argu - Frontend

## Requirements

-   NodeJS 18.12.1
-   npm 8.19.2

## Execution

To install the dependencies of the project, run:

```bash
> npm i
```

To run the project in development mode, navigate to the frontend directory and run:

```bash
> npm start
```

To launch the test runner, for all tests, run:

** For Linux/MacOS **

```bash
> ($env:CI = "true") -and (npm test)
```

** For Windows (Powershell) **

```bash
> CI=true npm run test
```

** For Windows (cmd.exe) **

```bash
> set CI=true&&npm test
```

To build the project in production mode, run:

```bash
> npm run build
```
