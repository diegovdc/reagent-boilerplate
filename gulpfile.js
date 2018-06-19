var watch_scss_path = './sass/',
    main_scss_path = watch_scss_path + '',
    bs_path = ''
/**
 * Required modules
 * @type {[]}
 */
var gulp = require('gulp');
var watch = require('gulp-watch');
var plumber = require("gulp-plumber");
var notify = require("gulp-notify");
var gutil = require("gulp-util");
var rename = require("gulp-rename");

//Sass
var sass = require('gulp-sass');
var autoprefixer = require('gulp-autoprefixer');


//sass and js sourcemaps
var sourcemaps = require('gulp-sourcemaps'); 


gulp.task('sass', function(){
  gulp.src(main_scss_path + 'mazorca.scss')
    .pipe(plumber({
        errorHandler: notify.onError({
          icon: './screenshot.png',
          message: "on line <%= error.message.split('on line')[1] %>",
          title: "Sass Error"
        })
      })
    )
    .pipe(sourcemaps.init())
    .pipe(sass()) // Using gulp-sass
    .pipe(rename("mazorca.css"))
    .pipe(notify('Mazorca ha sido compilado'))
    // .pipe(autoprefixer({
    //   browsers: ['last 6 versions'],
    //   cascade: false
    // }))
    .pipe(sourcemaps.write('./public/css/'))
    .pipe(gulp.dest('./public/css/'))
    // .pipe(browserSync.stream());
});


gulp.task('browser-sync', function() {
  browserSync.init(['./style.css'],{ //files to inject
     proxy: "localhost:8080" + bs_path
  });
});


gulp.task('watch', [/*'browser-sync',*/ 'sass'], function() {
  gulp.watch(watch_scss_path + '/**/*.scss', ['sass']); 
});







