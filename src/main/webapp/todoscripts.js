function Item(id, description, created, done) {
    this.id = id;
    this.description = description;
    this.created = created;
    this.done = done;
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

function validateAndCreate() {
    let valid = true;
    let description = document.getElementById('description').value;
    if (description === '') {
        valid = false;
        alert("Пожалуйста, заполните поле \"Задача\"");
    } else {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080//job4j_todo/item',
            data: {description: description},
            success: function () {
                alert("New task created!");
                location.reload();
            },
            error: function (err) {
                alert(err);
                console.log(err);
                valid = false;
            }
        })
    }
    return valid;
}

$(document).ready(function() {
    $('#showNotDone').click(function () {
        if ($(this).is(':checked')) {
            showItems(true);
        } else {
            showItems(false);
        }
    });
});

$(document).on('click', '#changeDoneItem', function() {
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
        url: 'http://localhost:8080//job4j_todo/itemupdate',
        data: {item: strItem},
        success: function () {
            location.reload();
        },
        error: function (err) {
            alert(err);
            console.log(err);
            valid = false;
        }
    })
}