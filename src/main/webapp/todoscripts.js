function Item(id, description, created, done, user) {
    this.id = id;
    this.description = description;
    this.created = created;
    this.done = done;
    this.user = user;
}

function User(login, password) {
    this.login = login;
    this.password = password;
}

$(document).ready(function () {
    showItems(false);
});

function showItems(hasAllItems) {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080/job4j_todo/items',
        dataType: "json",
        contentType: "text/json;charset=utf-8",
        success: function (respData) {

            let items = "";
            let itemsArr = [];
            for (let i = 0; i < respData.length; i++) {

                let curItem = respData[i];
                itemsArr.push(curItem);
                let categories = "";
                if (hasAllItems) {
                    for (let i = 0; i < curItem.categories.length; i++) {
                        categories += curItem.categories[i].name + " ";
                    }
                    if (curItem.done) {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curItem.id} id="changeDoneItem"
                                                                                       disabled checked></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                          <td>${curItem.user.login}</td>
                                          <td>${categories}</td>
                                          
                                      </tr>`;
                    } else {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curItem.id} id="changeDoneItem"></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                          <td>${curItem.user.login}</td>
                                          <td>${categories}</td>
                                      </tr>`;
                    }
                } else {
                    for (let i = 0; i < curItem.categories.length; i++) {
                        categories += curItem.categories[i].name + " ";
                    }
                    if (!curItem.done) {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curItem.id} id="changeDoneItem"></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                          <td>${curItem.user.login}</td>
                                          <td>${categories}</td>
                                      </tr>`;
                    }
                }
            }

            localStorage.setItem('items', JSON.stringify(itemsArr));
            $('#tbodyId').html(items);
        },
        error: function (err) {
            errorHandler(err);
        }
    })
}

function createItem() {
    let valid = true;
    let description = document.getElementById('description').value;
    let cIds = $("#categorySelect").val();
    if (description === '' || cIds.length === 0) {
        valid = false;
        alert("Пожалуйста, заполните все поля.");
    } else {
        let strCIds = JSON.stringify(cIds);
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/job4j_todo/item.do',
            data: ({description: description,
                    cIds: strCIds}),
            success: function () {
                alert("New task created!");
                location.reload();
            },
            error: function (err) {
                errorHandler(err, "Only authorized users can create tasks!");
                valid = false;
            }
        })
    }
    return valid;
}

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080/job4j_todo/categories',
        dataType: "json",
        success: function (respData) {
            let categories = "";
            for (let i = 0; i < respData.length; i++) {
                categories += "<option value=" + respData[i]['id'] + ">" + respData[i]['name'] + "</option>";
                // categories += "<option>" + respData[i]['name'] + "</option>";
            }
            $('#categorySelect').html(categories);
        },
        error: function (err) {
            alert(err);
        }
    })
});

$(document).ready(function () {
    $('#showNotDone').click(function () {
        if ($(this).is(':checked')) {
            showItems(true);
        } else {
            showItems(false);
        }
    });
});

$(document).ready(function () {
    let curLogin = sessionStorage.getItem('curUser');
    if (curLogin !== null) {
        $('#curLogin').after(`<p><a href="auth.html">${curLogin}</a></p>`);
    } else {
        $('#curLogin').after(`<a href="auth.html">Авторизация</a> | <a href="reg.html">Регистрация</a>`);
    }
})

$(document).on('click', '#changeDoneItem', function () {
    let curId = $(this).val();
    if ($(this).is(':checked')) {
        updateItem(true, curId);
    } else {
        updateItem(false, curId);
    }
});

function updateItem(hasDone, curId) {
    let items = JSON.parse(localStorage.getItem('items'));
    let curItem = null;
    for (let i = 0; i < items.length; i++) {
        if (items[i]['id'] == curId) {
            curItem = items[i];
            curItem.done = hasDone;
            break;
        }
    }
    let strItem = JSON.stringify(curItem);
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/job4j_todo/itemupdate.do',
        data: {item: strItem},
        success: function () {
            location.reload();
        },
        error: function (err) {
            errorHandler(err, "Only authorized users can update task status!");
        }
    })
}

function getInputUser() {
    let login = document.getElementById('login').value;
    let password = document.getElementById('password').value;
    return new User(login, password);
}

function validateUserData() {
    let valid = true;
    let user = getInputUser();
    if (user.login === '') {
        valid = false;
        alert("Нужно заполнить поле \"Логин\"");
    } else if (user.password === '') {
        valid = false;
        alert("Нужно заполнить поле \"Пароль\"");
    }
    return valid;
}

function createUser() {
    let isValid = validateUserData();
    if (isValid) {
        let strUser = JSON.stringify(getInputUser());
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/job4j_todo/reg',
            data: {user: strUser},
            dataType: "json",
            success: function (response) {
                let login = response.login;
                if (login !== undefined) {
                    sessionStorage.setItem('curUser', login);
                    window.location.replace("index.html");
                } else {
                    alert(response);
                }
            },
            error: function (err) {
                errorHandler(err);
                isValid = false;
            }
        })
    }

    return isValid;
}

function authUser() {
    let isValid = validateUserData();
    if (isValid) {
        let user = getInputUser();
        let strUser = JSON.stringify(user);
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/job4j_todo/auth',
            data: {user: strUser},
            dataType: "json",
            success: function (response) {
                let login = response.login;
                if (login !== undefined) {
                    sessionStorage.setItem('curUser', login);
                    window.location.href = "index.html";
                } else {
                    alert(response);
                }
            },
            error: function (err) {
                errorHandler(err, "Wrong login or password!");
                isValid = false;
            }
        })
    }
    return isValid;
}

function errorHandler(err, authMsg) {
    switch (err.status) {
        case 401:
            alert(authMsg);
            window.location.replace("auth.html");
            break;
        case 400:
            alert("Bad request!");
            break;
        case 500:
            alert("Internal server error!");
            break;
        default:
            alert(err.status + " " + err.responseText);
            console.log(err.status + " " + err.responseText);
    }
}