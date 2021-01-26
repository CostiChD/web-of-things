let baseUrl = 'http://ec2-18-196-187-178.eu-central-1.compute.amazonaws.com:8082';

(function () {
    createDeviceExitButtonListener();
    registerDeviceListener();
	fetchDevices();
})();

function registerDeviceListener() {
	document.getElementById('register-device').addEventListener("click", function (e) {
			fetch(baseUrl + '/devices/' + document.getElementById("serial-number").value, {
					method: "POST",
					headers: {
						'Content-Type': 'application/json'
					}
				})
				.then(resp => resp.json())
				.then(myJson => {
					window.location.href = "../admin/devices.html";
				})
				.catch(err => {
					console.log(err);
				});
		});
}

function createDeviceExitButtonListener() {
	document.getElementsByClassName("exit-button-container")[0].addEventListener("click", function(e) {
		document.getElementsByClassName("devices-container")[0].style.display = "block";
		document.getElementsByClassName("device-container")[0].style.display = "none";

		document.getElementsByClassName("page-title")[0].innerHTML = "Devices";
	});
}

function fetchDevices() {
	let tbody = document.getElementById("devices-list");
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
                
                fetch(baseUrl + '/groups/devices?userEmail=' + sessionStorage.getItem("email") +'&groupName=' + element.group.name, {
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
                                groupName: element.group.name,
                                deviceName: device.name,
                                groupAdminEmail: element.groupAdminEmail
                            });
                        });
    
                        let deleteButton = document.createElement('button');
                        deleteButton.innerHTML = "<i class='fas fa-trash'></i>";
                        deleteButton.className = "button button-icon hover-danger";
                        deleteButton.id = device.name;
    
                        deleteButton.addEventListener("click", function (e) {
                                deleteDevice({
                                groupName: element.group.name,
                                deviceName: device.name,
                                groupAdminEmail: element.groupAdminEmail
                            });
                        });
    
                        actions.appendChild(viewButton);
                        actions.appendChild(deleteButton);
    
                        tr.appendChild(groupName);
                        tr.appendChild(actions);
    
                        tbody.appendChild(tr);
    
                        if (element.groupAdminEmail !== sessionStorage.getItem("email")) {
                            document.getElementById(device.name).style.display = "none";
                        }
                    });
    
                })
                .catch(err => {
                    console.log(err)
                });
			});
		})
		.catch(err => {
			console.log(err);
		});
}

function seeDevice(response) {

    document.getElementsByClassName("devices-container")[0].style.display = "none";
    let deviceContainer = document.getElementsByClassName("device-container")[0];
    deviceContainer.style.display = "block";

    deviceContainer.removeChild(deviceContainer.lastChild);


	fetch(baseUrl + '/devices/' + response.deviceName, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(resp => resp.json())
    .then(myJson => {
        let device = myJson;
        let dev = document.createElement('div');
        dev.className = "device";

        let deviceName = document.createElement("h1");
        deviceName.id = "device-name";
        deviceName.innerText = device.name;

        let descriptionTitle = document.createElement("h2");
        descriptionTitle.id = "description-title";
        descriptionTitle.innerHTML = "Description:";
        let description = document.createElement("p");
        description.id = "description";
        description.innerHTML = device.description; 

        let deviceDetails = document.createElement("div");
        deviceDetails.className = "device-details";

        let deviceProperties = document.createElement("ul");
        deviceProperties.className = "device-properties";

        let propertiesTitle = document.createElement("h2");
        propertiesTitle.innerHTML = "Properties:";
        deviceProperties.appendChild(propertiesTitle);

        device.properties.forEach(property => {
            let prop = document.createElement("li");
            prop.className = "device-property";

            let name = document.createElement("h3");
            name.innerHTML = property.name;

            let value = document.createElement("p");
            value.innerHTML = "Value: " + property.value;

            let sourceLink = document.createElement("p");
            sourceLink.innerHTML = "Source link: " + property.sourceLink;

            prop.appendChild(name);
            prop.appendChild(value);
            prop.appendChild(sourceLink);
            deviceProperties.appendChild(prop);
        });

        let deviceEvents = document.createElement("ul");
        deviceEvents.className = "device-events";

        let eventsTitle = document.createElement("h2");
        eventsTitle.innerHTML = "Events:";
        deviceEvents.appendChild(eventsTitle);

        device.events.forEach(event => {
            let ev = document.createElement("li");
            ev.className = "device-event";

            let name = document.createElement("h3");
            name.innerHTML = event.name;

            let description = document.createElement("p");
            description.innerHTML = "Description: " + event.description;

            let sourceLink = document.createElement("p");
            sourceLink.innerHTML = "Source link: " + event.sourceLink;

            ev.appendChild(name);
            ev.appendChild(description);
            ev.appendChild(sourceLink);

            deviceEvents.appendChild(ev);
        });

        let deviceActions = document.createElement("div");
        deviceActions.className = "device-actions";

        device.actions.forEach(action => {
            let act = document.createElement("div");
            act.className = "device-action";

            // let name = document.createElement("h3");
            // name.innerHTML = action.name;

            // let type = document.createElement("p");
            // type.innerHTML = "Type:" + action.type;

            // let sourceLink = document.createElement("p");
            // sourceLink.innerHTML = "Source link: " + action.sourceLink;

            // act.appendChild(name);
            // act.appendChild(type);
            // act.appendChild(sourceLink);

            if (action.type === "SWITCHABLE") {
                let input = document.createElement("input");
                input.type = "checkbox";
                input.id = device.name;

                let label = document.createElement("label");
                label.htmlFor = device.name;
                label.className = "switch";

                act.appendChild(input);
                act.appendChild(label);
            }

            if (action.type === "ADJUSTABLE") {

            }

            deviceActions.appendChild(act);
        });

        deviceDetails.appendChild(deviceName);
        deviceDetails.appendChild(descriptionTitle);
        deviceDetails.appendChild(description);
        deviceDetails.appendChild(deviceProperties);
        deviceDetails.appendChild(deviceEvents);

        dev.appendChild(deviceDetails);
        dev.appendChild(deviceActions);

        deviceContainer.appendChild(dev);
    })
    .catch(err => {
        console.log(err)
    });
}