module.exports = {
    outputDir: 'oefront',
    pages: {
        index:{
            entry: 'src/modules/index/index.js',
            template: 'public/index.html',
            filename: 'index.html',
            title: 'index page',
        },
        manage: {
            entry: 'src/modules/manage/manage.js',
            template: 'public/manage.html',
            filename: 'manage.html',
            title: 'manage page',
        },
    },
    devServer: {
        proxy: {
            '/online-edu': {
                target: "http://202.120.40.8:30382",
                ws: true,
                changeOrigin: true,
            }
        }
    },
};
