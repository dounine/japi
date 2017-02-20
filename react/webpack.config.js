var webpack = require('webpack');
var openBrowserWebpackPlugin = require('open-browser-webpack-plugin');//自动打开浏览器
var path = require('path');

module.exports = {
    devServer: {
        historyApiFallback: true,
        hot: true,
        inline: true,
        progress: true,
        // port: port //配置端口号
    },
    entry: {
        main: [
            'webpack-dev-server/client?http://0.0.0.0:8080',
            'webpack/hot/only-dev-server',
            './src/index.js'],
    },
    output: {
        path: './dist',
        publicPath: '/dist/',
        filename: '[name].js'
    },
    resolve: {
        extensions: ['', '.js', '.jsx']
    },
    watch: true,
    module: {
        loaders: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel'
            },{
                test: /\.css$/,
                loader: 'style-loader!css-loader?sourceMap'
            },{
                test: /\.scss$/,
                loader: "style!css!sass?sourceMap"
            },{
                test: /\.(png|jpg|woff|woff2|eot|ttf|svg)$/,
                loader: 'url-loader?limit=8192'
            },{
                test: /\.json$/,
                loader: 'json-loader'
            },{
                test: /\.(png|jpg|gif)$/,
                loader: 'url-loader?limit=819200'
            }
        ]
    },
    babel: {
        presets: ['es2015',  'react'],
    },
    plugins: [
        // new webpack.HotModuleReplacementPlugin()
        new openBrowserWebpackPlugin({ url: 'http://localhost:8080/webpack-dev-server/' })//自动打开其浏览器
    ]
};