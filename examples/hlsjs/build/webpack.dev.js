/* eslint-disable @typescript-eslint/no-var-requires */
const { merge } = require("webpack-merge");
const common = require("./webpack.common");

module.exports = merge(common, {
    devServer: {
        contentBase: "./dist",
        open: false,
        https: true,
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Credentials": "true",
            "Access-Control-Allow-Methods": "GET,POST",
            "Access-Control-Allow-Headers":
                "Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers",
        },
    },
    devtool: "inline-source-map",
    mode: "development",
});
