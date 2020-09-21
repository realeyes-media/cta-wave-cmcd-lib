/* eslint-disable @typescript-eslint/no-var-requires */
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require("path");

module.exports = {
    context: __dirname,
    entry: "../src/index.ts",
    module: {
        rules: [
            {
                test: /\.(ts|js)$/,
                exclude: /node_modules/,
                loader: "babel-loader",
            },
        ],
    },
    output: {
        filename: "main.js",
        path: path.resolve(__dirname, "../dist"),
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            template: "../public/index.html",
            favicon: "../public/resources/favicon-32x32.png",
        }),
    ],
    resolve: {
        extensions: [".ts", ".js"],
    },
};
