let baseUrl = 'http://ec2-3-122-225-224.eu-central-1.compute.amazonaws.com';

(function () {
	createGroupExitButtonListener();
	createGroupListener();
	fetchGroups();
})();

function createGroupListener() {
	document.getElementById('create').addEventListener("click", function (e) {
			fetch(baseUrl + '/groups', {
					method: "POST",
					body: JSON.stringify({
						group: {
							name: document.getElementById("group-name").value.trim() === "" ? "Unknown group" + Math.floor(Math.random() * 10000) : document.getElementById("group-name").value
						},
						adminEmail: sessionStorage.getItem("email")
					}),
					headers: {
						'Content-Type': 'application/json'
					}
				})
				.then(resp => resp.json())
				.then(myJson => {
					window.location.href = "../admin/groups.html";
				})
				.catch(err => {
					console.log(err);
				});
		});
}

function createGroupExitButtonListener() {
	document.getElementsByClassName("exit-button-container")[0].addEventListener("click", function(e) {
		document.getElementsByClassName("groups-container")[0].style.display = "block";
		document.getElementsByClassName("group-container")[0].style.display = "none";

		document.getElementsByClassName("page-title")[0].innerHTML = "Groups";
	});
}

function fetchGroups() {
	let tbody = document.getElementById("groups-list");
	tbody.innerHTML = '';

	fetch(baseUrl + '/groups/users?userEmail=' + sessionStorage.getItem("email"), {
			method: "GET",
			headers: {
				'Content-Type': 'application/json'
			}
		})
		.then(function (response) {
			return response.json();
		})
		.then(function (result) {
			let groups = result.groups;
			groups.forEach(element => {
				let tr = document.createElement('tr');

				let groupName = document.createElement('td');
				groupName.innerHTML = element.group.name;

				let actions = document.createElement('td');
				let viewButton = document.createElement('button');
				viewButton.innerHTML = "<i class='fas fa-eye'></i>";
				viewButton.className = "button button-icon hover-info";
				
				viewButton.addEventListener("click", function (e) {
						seeGroup({
						name: element.group.name,
						groupAdminEmail: element.groupAdminEmail
					});
				});

				let deleteButton = document.createElement('button');
				deleteButton.innerHTML = "<i class='fas fa-trash'></i>";
				deleteButton.className = "button button-icon hover-danger";
				deleteButton.id = element.group.name;

				deleteButton.addEventListener("click", function (e) {
						deleteGroup({
						name: element.group.name,
						groupAdminEmail: element.groupAdminEmail
					});
				});

				let addUserButton = document.createElement('button');
				addUserButton.innerHTML = "<i class='fas fa-user-plus'></i>";
				addUserButton.className = "button button-icon hover-green";
				addUserButton.id = "add-user-button";

				$(document).ready(function() {
					addUserButton.addEventListener("click", function (e) {
						$('#overlay-add-user').fadeIn(300);
					});

					$('#close-add-user').click(function() {
					   $('#overlay-add-user').fadeOut(300);
					});

					$('#add-user-submit-button').click(() => {
						addUserToGroup({
							groupName: element.group.name,
							groupAdminEmail: element.groupAdminEmail
						});
					});
				 });

				let addPermissionButton = document.createElement('button');
				addPermissionButton.innerHTML = "<i class='fas fa-plus-circle'></i>";
				addPermissionButton.className = "button button-icon hover-green";
				addPermissionButton.id = "add-permission-button";

				$(document).ready(function() {
					addPermissionButton.addEventListener("click", function (e) {
						$('#overlay-add-permission').fadeIn(300);
					});

					$('#close-add-permission').click(function() {
					   $('#overlay-add-permission').fadeOut(300);
					});

					$('#add-permission-submit-button').click(() => {
						addPermissionToGroup({
							groupName: element.group.name,
							groupAdminEmail: element.groupAdminEmail
						});
					});
				 });
				

				actions.appendChild(viewButton);
				actions.appendChild(addUserButton);
				actions.appendChild(addPermissionButton);
				actions.appendChild(deleteButton);

				tr.appendChild(groupName);
				tr.appendChild(actions);

				tbody.appendChild(tr);

				if (element.groupAdminEmail !== sessionStorage.getItem("email")){
					document.getElementById(element.group.name).style.display = "none";
				}
			});
		})
		.catch(err => {
			console.log(err);
		});
}

function deleteGroup(response) {
	var r = confirm("Are you sure you want to delete group " + response.name + " ?");
	if (r == true) {
		fetch(baseUrl + '/groups/' + response.name, {
				method: "DELETE",
				headers: {
					'Content-Type': 'application/json'
				}
			})
			.then(resp => resp)
			.then(respp => {
				window.location.href = "../admin/groups.html";
			})
			.catch(err => {
				console.log(err);
			});
	}
}

function addPermissionToGroup(response) {
	fetch(baseUrl + '/permissions', {
			method: "POST",
			body: JSON.stringify({
				adminEmail: response.groupAdminEmail,
				permission: {
					groupName: response.groupName,
					deviceName: $('#device-name').val()
				}
			}),
			headers: {
				'Content-Type': 'application/json'
			}
		})
		.then(resp => resp)
		.then(respp => {
			window.location.href = "../admin/groups.html";
		})
		.catch(err => {
			console.log(err);
		});
}

function addUserToGroup(response) {
	fetch(baseUrl + '/groups/users', {
			method: "POST",
			body: JSON.stringify({
				adminEmail: response.groupAdminEmail,
    			userEmailToAdd: $('#user-email').val(),
    			groupName: response.groupName
			}),
			headers: {
				'Content-Type': 'application/json'
			}
		})
		.then(resp => resp)
		.then(respp => {
			window.location.href = "../admin/groups.html";
		})
		.catch(err => {
			console.log(err);
		});
}


function seeGroup(response) {
	document.getElementsByClassName("groups-container")[0].style.display = "none";
	document.getElementsByClassName("group-container")[0].style.display = "block";

	document.getElementsByClassName("page-title")[1].innerHTML = response.name;

	let tbody = document.getElementById("devices-list");
	tbody.innerHTML = '';

	fetch(baseUrl + '/groups/devices?userEmail=' + sessionStorage.getItem("email") +'&groupName=' + response.name, {
				method: "GET",
				headers: {
					'Content-Type': 'application/json'
				}
			})
			.then(resp => resp.json())
			.then(myJson => {
				myJson.devices.forEach(device => {
					let tr = document.createElement('tr');
					
					let groupName = document.createElement('td');
					groupName.innerHTML = device.name;

					let actions = document.createElement('td');
					let viewButton = document.createElement('button');
					viewButton.innerHTML = "<i class='fas fa-eye'></i>";
					viewButton.className = "button button-icon hover-info";
					
					viewButton.addEventListener("click", function (e) {
							seeDevice({
							groupName: response.name,
							deviceName: device.name,
							groupAdminEmail: response.groupAdminEmail
						});
					});

					let deleteButton = document.createElement('button');
					deleteButton.innerHTML = "<i class='fas fa-trash'></i>";
					deleteButton.className = "button button-icon hover-danger";
					deleteButton.id = device.name;

					deleteButton.addEventListener("click", function (e) {
							deleteDevice({
							groupName: response.name,
							deviceName: device.name,
							groupAdminEmail: response.groupAdminEmail
						});
					});

					actions.appendChild(viewButton);
					actions.appendChild(deleteButton);

					tr.appendChild(groupName);
					tr.appendChild(actions);

					tbody.appendChild(tr);

					if (response.groupAdminEmail !== sessionStorage.getItem("email")) {
						document.getElementById(device.name).style.display = "none";
					}
				});

			})
			.catch(err => {
				console.log(err)
			});
}

function seeDevice(response) {
	sessionStorage.setItem("device-name", response.deviceName);
	window.location.href = "../admin/devices.html";
}

function deleteDevice(response) {
	var r = confirm("Are you sure you want to delete " + response.deviceName + " from " + response.groupName + "?");
	if (r == true) {
		fetch(baseUrl + '/permissions?groupName='+ response.groupName + '&deviceName=' + response.deviceName, {
				method: "DELETE",
				headers: {
					'Content-Type': 'application/json'
				}
			})
			.then(resp => resp)
			.then(respp => {
				seeGroup({
					name: response.groupName,
					groupAdminEmail: response.groupAdminEmail
				});
			})
			.catch(err => {
				console.log(err);
			});
	}
}