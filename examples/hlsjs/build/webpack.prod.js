/* eslint-disable @typescript-eslint/no-var-requires */
const { merge } = require("webpack-merge");
const common = require("./webpack.common");

module.exports = merge(common, {
    devtool: "source-map",
    mode: "production",
});
