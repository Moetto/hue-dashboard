const mainCssFile = 'styles.css';

const tailwind = {
    plugins: [],
    variants: {},
    theme: {
        extend: {},
    },
    content: {
        files: [
            '*.{js,html,css}',
            './kotlin/**/*.{js,html,css}'
        ],
        transform: {
            js: (content) => {
                return content.replaceAll(/(\\r)|(\\n)|(\\r\\n)/g, ' ')
            }
        }
    },
};

((config) => {
    let entry = '/kotlin/' + mainCssFile;
    config.entry.main.push(entry);
    config.module.rules.push({
        test: /\.css$/,
        use: [
            {loader: 'style-loader'},
            {loader: 'css-loader'},
            {
                loader: 'postcss-loader',
                options: {
                    postcssOptions: {
                        plugins: [
                            require("tailwindcss")({config: tailwind}),
                            require("autoprefixer"),
                            require("cssnano")
                        ]
                    }
                }
            }
        ]
    });
})(config);
