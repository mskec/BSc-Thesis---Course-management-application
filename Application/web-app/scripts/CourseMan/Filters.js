angular.module('CourseMan')
  .filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    };
  }])
  .filter('truncate', function() {
    return function (text, length, ending) {
      if (isNaN(length)) {
        length = 10;
      }

      if (ending === undefined) {
        ending = "...";
      }

      if (text.length <= length || text.length - ending.length <= length) {
        return text;
      }
      else {
        return String(text).substring(0, length - ending.length) + ending;
      }
    };
  })
  .filter('myDate', ['$filter', function($filter) {
    return function(date) {
      return $filter('date')(date, 'HH:mm dd.MM.yyyy')
    }
  }]);
