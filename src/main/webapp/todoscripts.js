function Item(id, description, created, done) {
    this.id = id;
    this.description = description;
    this.created = created;
    this.done = done;
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
        url: 'http://localhost:8080//job4j_todo/items',
        dataType: "json",
        success: function (respData) {

            let items = "";
            let itemsArr = [];
            for (let i = 0; i < respData.length; i++) {
                let curId = respData[i]['id'];
                let curDescription = respData[i]['description'];
                let curCreated = respData[i]['created'];
                let hasCurDone = respData[i]['done'];
                let curItem = new Item(curId, curDescription, curCreated, hasCurDone);

                itemsArr.push(curItem);

                if (hasAllItems) {
                    if (hasCurDone) {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curId} id="changeDoneItem"
                                                                                       disabled checked></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                      </tr>`;
                    } else {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curId} id="changeDoneItem"></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                      </tr>`;
                    }
                } else {
                    if (!hasCurDone) {
                        items += `<tr>
                                          <td><input type="checkbox" value=${curId} id="changeDoneItem"></td>
                                          <td>${curItem.description}</td>
                                          <td>${curItem.created}</td>
                                      </tr>`;
                    }
                }
            }

            localStorage.setItem('items', JSON.stringify(itemsArr));
            $('#tbodyId').html(items);
        },
        error: function (err) {
            alert(err);
        }
    })
}

function createItem() {
    let valid = true;
    let description = document.getElementById('description').value;
    if (description === '') {
        valid = false;
        alert("Пожалуйста, заполните поле \"Задача\"");
    } else {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080//job4j_todo/item.do',
            data: {description: description},
            success: function () {
                alert("New task created!");
                location.reload();
            },
            error: function (err) {
                if (err.status === 401) {
                    alert("Only authorized users can create tasks!");
                    window.location.replace("auth.html");
                } else {
                    alert(err.responseText);
                    console.log(err.responseText);
                }
                valid = false;
            }
        })
    }
    return valid;
}

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
        $('#curLogin').after(`<a href="auth.html">Авторизация</a><a href="reg.html">Регистрация</a>`);
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
            let curId = items[i]['id'];
            let curDescription = items[i]['description'];
            let curCreated = items[i]['created'];
            curItem = new Item(curId, curDescription, curCreated, hasDone);
            break;
        }
    }
    let strItem = JSON.stringify(curItem);
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080//job4j_todo/itemupdate.do',
        data: {item: strItem},
        success: function () {
            location.reload();
        },
        error: function (err) {
            errorHandler(err);
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
            url: 'http://localhost:8080//job4j_todo/reg.do',
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
                alert(err.status);
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
            url: 'http://localhost:8080//job4j_todo/auth.do',
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
            error: function(err) {
                errorHandler(err);
                isValid = false;
            }
        })
    }
    return isValid;
}

function errorHandler(err) {
    switch (err.status) {
        case 401:
            alert("Wrong login or password!");
            break;
        case 400:
            alert("Bad request!");
            break;
        case 500:
            alert("Internal server error!");
            break;
        default:
            alert(err.responseText);
            console.log(err.responseText);
    }
}