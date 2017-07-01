angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('fightCrimes', {
    url: '/page1',
    templateUrl: 'templates/fightCrimes.html',
    controller: 'fightCrimesCtrl'
  })

  .state('signUp', {
    url: '/page2',
    templateUrl: 'templates/signUp.html',
    controller: 'signUpCtrl'
  })

  .state('login', {
    url: '/page5',
    templateUrl: 'templates/login.html',
    controller: 'loginCtrl'
  })

  .state('profile', {
    url: '/page8',
    templateUrl: 'templates/profile.html',
    controller: 'profileCtrl'
  })

  .state('postCrimeScenes', {
    url: '/page9',
    templateUrl: 'templates/postCrimeScenes.html',
    controller: 'postCrimeScenesCtrl'
  })

  .state('page', {
    url: '/page10',
    templateUrl: 'templates/page.html',
    controller: 'pageCtrl'
  })

  .state('cases', {
    url: '/page11',
    templateUrl: 'templates/cases.html',
    controller: 'casesCtrl'
  })

  .state('shareInformation', {
    url: '/page12',
    templateUrl: 'templates/shareInformation.html',
    controller: 'shareInformationCtrl'
  })

$urlRouterProvider.otherwise('/page1')

  

});