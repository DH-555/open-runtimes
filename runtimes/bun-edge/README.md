# Bun Runtime (edge)

This is the Open Runtime that runs TypeScript (or JavaScript) code based on a `jarredsumner/bun:edge` base image. 

To learn more about runtimes, visit [Structure](https://github.com/open-runtimes/open-runtimes#structure) section of the main README.md.

## Usage

1. Create a folder and enter it. Add code into `mod.ts` file:

```bash
mkdir bun-or && cd bun-or
printf "module.exports = async (req, res) => {\n    res.json({ n: Math.random() })\n}" > index.js
```

2. Build the code:

```bash
docker run -e INTERNAL_RUNTIME_ENTRYPOINT=index.ts --rm --interactive --tty --volume $PWD:/usr/code openruntimes/bun:edge sh /usr/local/src/build.sh
```

3. Spin-up open-runtime:

```bash
docker run -p 3000:3000 -e INTERNAL_RUNTIME_KEY=secret-key -e INTERNAL_RUNTIME_ENTRYPOINT=index.ts --rm --interactive --tty --volume $PWD/code.tar.gz:/tmp/code.tar.gz:ro openruntimes/bun:edge sh /usr/local/src/start.sh
```

4. In new terminal window, execute function:

```bash
curl -H "X-Internal-Challenge: secret-key" -H "Content-Type: application/json" -X POST http://localhost:3000/ -d '{"payload": "{}"}'
```

Output `{"n":0.7232589496628183}` with random float will be displayed after the execution.

## Local development

1. Clone the [open-runtimes](https://github.com/open-runtimes/open-runtimes) repository:

```bash
git clone https://github.com/open-runtimes/open-runtimes.git
```

2. Enter the Bun runtime folder:

```bash
cd open-runtimes/runtimes/bun-edge
```

3. Run the included example cloud function:

```bash
docker-compose up -d
```

4. Execute the function:

```bash
curl -H "X-Internal-Challenge: secret-key" -H "Content-Type: application/json" -X POST http://localhost:3000/ -d '{"payload": "{}"}'
```

You can now send `POST` request to `http://localhost:3000`. Make sure you have header `x-internal-challenge: secret-key`. If your function expects any parameters, you can pass an optional JSON body like so: `{ "payload":"{}" }`.

You can also make changes to the example code and apply the changes with the `docker-compose restart` command.

## Notes

- When writing function for this runtime, ensure it is exported as default one. An example of this is:

```typescript
export default async function(req: any, res: any) {
    res.send('Hello Open Runtimes 👋');
}
```

- The `res` parameter has two methods:

    - `send()`: Send a string response to the client.
    - `json()`: Send a JSON response to the client.

You can respond with `json()` by providing object:

```typescript
export default async function(req: any, res: any) {
    res.json({
        'message': 'Hello Open Runtimes 👋',
        'env': req.env,
        'payload': req.payload,
        'headers': req.headers
    });
}
```

- Dependencies are handeled automatically. Open Runtimes automatically cache and install them during build process.

- The default entrypoint is `mod.ts`. If your entrypoint differs, make sure to configure it using `INTERNAL_RUNTIME_ENTRYPOINT` environment variable, for instance, `INTERNAL_RUNTIME_ENTRYPOINT=src/app.ts`.

## Authors

**Eldad Fux**

+ [https://twitter.com/eldadfux](https://twitter.com/eldadfux)
+ [https://github.com/eldadfux](https://github.com/eldadfux)

**Bradley Schofield**

+ [https://github.com/PineappleIOnic](https://github.com/PineappleIOnic)

**Matej Bačo**

+ [https://github.com/Meldiron](https://github.com/Meldiron)

## Contributing

For security issues, please email security@appwrite.io instead of posting a public issue in GitHub.

You can refer to the [Contributing Guide](https://github.com/open-runtimes/open-runtimes/blob/main/CONTRIBUTING.md) for more info.
