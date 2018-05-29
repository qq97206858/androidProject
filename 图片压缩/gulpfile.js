var gulp = require('gulp');
var tiny = require('gulp-tinypng-nokey');

gulp.task('tinypng', function(cb) {
    gulp.src('./*')
        .pipe(tiny())
        .pipe(gulp.dest('dist'));
});