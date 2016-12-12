/**
 * 
 */

function getContextPath() {
	var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	alert("contextPath = "+contextPath);
	return contextPath;
}

function displayMessage(cssClass, message) {
	var header = '';
	if('error' == cssClass) {
		header = 'Error!';
	} else {
		header = 'Success!';
	}
	$('#alertModal').find('.modal-title').text(header);
	removeCss();
	$('#cssClass').addClass(cssClass);
    $('#alertModal').find('.modal-body p').html(message);
    $('#alertModal').modal('show');
}

function removeCss() {
	$('#cssClass').removeClass('error');
	$('#cssClass').removeClass('msg');
}

var connectorApp = angular.module('connectorApp', [ 'ngResource'])
		.directive(
				'loading',
				function() {
					return {
						restrict : 'E',
						replace : true,
						template : '<div class="loading"><img th:src="@{/js/ajax-loader.gif}" width="20" height="20" />LOADING...</div>',
						link : function(scope, element, attr) {
							scope.$watch('loading', function(val) {
								if (val)
									$(element).show();
								else
									$(element).hide();
							});
						}
					}
				});

connectorApp
.controller(
		'connectorController',['$scope', '$http', '$resource','$location',
		function($scope, $http, $resource, $location) {
			$scope.isImpact = false;
			$scope.isWatson = false;
			$scope.isHome = true;
			$scope.impactDetails = {};
			$scope.watsonDetails = {};
			$scope.contextPath = window.location.pathname;
			$scope.loading = false;
			$scope.showRegister = false;
			
			var clear = function() {
				$scope.isImpact = false;
				$scope.isWatson = false;
				$scope.isHome = false;
				$scope.impactDetails = {};
				$scope.watsonDetails = {};
				$scope.loading = false;
				$scope.showRegister = false;
			}
			
			$scope.showHome = function() {
				clear();
				$scope.isHome = true;
			}
			
			$scope.showImpact = function() {
				clear();
				$scope.isImpact = true;
			}
			
			$scope.showWatson = function() {
				clear();
				$scope.isWatson = true;
			}
			
			$scope.submitImpactCred = function() {
				$scope.loading = true;
				var impactDataObj = {
					impactUsername : $scope.impactDetails.impactUsername,
					impactPassword : CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse($scope.impactDetails.impactPassword)),
					impactGroupName : $scope.impactDetails.groupName
				};
				var impactRes = $http.post($scope.contextPath+'/inbound/impactproperty', impactDataObj);
				impactRes
						.success(function(data, status, headers, config) {
							console.log("response = "+data.msg);
							$scope.loading = false;
							$scope.showRegister = true;
							//$scope.message = "Authentication Saved Successfully";
							//$scope.showSubmit = false;
							/*setAuthenticationObj(data);
							$scope.showAuthSend = true;*/
							var message = data.msg;
							displayMessage('msg',message);
							//$scope.cssClass = "msg";
							
						});
				impactRes.error(function(data, status, headers, config) {
					$scope.loading = false;
					var message = 'Failed to Save IMPACT Credentials';
					displayMessage('error',message);
				});
			}
			
			$scope.submitWatsonCred = function() {
				$scope.loading = true;
				var watsonDataObj = {
						watsonApiKey : $scope.watsonDetails.watsonApiKey,
						watsonApiToken : $scope.watsonDetails.watsonApiToken,
						watsonOrgId : $scope.watsonDetails.watsonOrgId
				};
				var watsonRes = $http.post($scope.contextPath+'/inbound/watsonproperty', watsonDataObj);
				watsonRes
						.success(function(data, status, headers, config) {
							$scope.loading = false;
							console.log("response = "+data.msg)
							//$scope.message = "Authentication Saved Successfully";
							//$scope.showSubmit = false;
							/*setAuthenticationObj(data);
							$scope.showAuthSend = true;*/
							var message = data.msg;
							displayMessage('msg',message);
							//$scope.cssClass = "msg";
							
						});
				watsonRes.error(function(data, status, headers, config) {
					$scope.loading = false;
					var message = 'Failed to Save WATSON Credentials';
					displayMessage('error',message);
				});
			}
			
			$scope.registerToImpact = function() {
				$('#alertModal').modal('hide');
				$scope.loading = true;
				var regisRes = $http.post($scope.contextPath+'/impact/register');
				regisRes
						.success(function(data, status, headers, config) {
							$scope.loading = false;
							console.log("response = "+data.msg)
							//$scope.message = "Authentication Saved Successfully";
							//$scope.showSubmit = false;
							/*setAuthenticationObj(data);
							$scope.showAuthSend = true;
							var message = 'Registration saved Successfully';*/
							displayMessage('msg',data.msg);
							$scope.cssClass = "msg";
							
						});
				regisRes.error(function(data, status, headers, config) {
					$scope.loading = false;
					var message = 'Failed to Register with IMPACT';
					displayMessage('error',message);
				});
			}
			
		}]);