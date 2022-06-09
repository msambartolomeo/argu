M.AutoInit();

document.addEventListener('DOMContentLoaded', function() {
    const datepicker = document.querySelectorAll(".datepicker");
    M.Datepicker.init(datepicker, {
        format: 'dd-mm-yyyy',
    });
});

function addParamToUrlAndRedirect(name, param) {
    const url = new URL(window.location.href);
    url.searchParams.delete('page');
    if (param === "") {
        url.searchParams.delete(name);
    } else {
        url.searchParams.set(name, param);
    }
    window.location.href = url.href;
}

function dateFilter(date) {
    const url = new URL(window.location.href.replace(new RegExp("/\\d+(\\?.*)?"), ""));
    const parts = date.split(" ")[1].split("/")
    const dateString = parts[0] + "-" + parts[1] + "-" + parts[2];
    url.searchParams.append('date', dateString);
    window.location.href = url.href;
}

function resetFileValue(fileId, fileNameId) {
    document.getElementById(fileId).value = "";
    const fileName = document.getElementById(fileNameId);
    fileName.value = "";
    fileName.classList.remove("valid");
    document.getElementById("x").classList.add("x");
}

function updateVisibilityOfX() {
    document.getElementById("x").classList.remove("x");
}

$(document).ready(() => {
    $("form").submit((e) => {
        $(".submitBtn").attr("disabled", true);
    });
});