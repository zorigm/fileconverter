// Function to handle file selection and conversion

var apiUrl = 'http://192.82.66.43:9094';
apiUrl = 'http://localhost:8080';
var convertedFileName = "";
var convertedHtmlRaw = "";
var convertedFileType  = "";

function convertFile() {
    // Get the selected file
    var fileUpload = document.getElementById('fileUpload');
    var file = fileUpload.files[0];

    convertedFileName = file.name.substr(0,file.name.lastIndexOf("."));
    if (fileUpload.files.length == 0) {
        /* alert('Please select a HWP, HWPX, DOC, DOCX, XLS, XLSX or PDF file.');*/
        alert('Please select a HWP,DOC, DOCX, XLS, XLSX or PDF file.');
        return;
    }
    if (file.size >= 26214400) {
        alert('We allows files up to 25MB.');
        return;
    }

    // Validate file type (only HWP, HWPX, DOC, DOCX, XLS, XLSX or PDF allowed)
    //var allowedTypes = ['hwp', 'hwpx', 'doc', 'docx', 'pdf', 'xls', 'xlsx', ];
    var allowedTypes = ['hwp', 'hwpx', 'doc', 'docx', 'pdf', 'xls', 'xlsx' ];

    if (!allowedTypes.includes(file.name.substr(file.name.lastIndexOf(".") + 1).toLowerCase())) {
        /*alert('Please select a HWP, HWPX, DOC, DOCX, XLS, XLSX or PDF file.');*/
        alert('Please select a HWP,DOC, DOCX, XLS, XLSX or PDF file.');
        return;
    }
    // Create a FormData object to send the file to the server
    var formData = new FormData();
    formData.append('file', file);
    var fileType = file.name.substr(file.name.lastIndexOf(".") + 1);
    convertedFileType = fileType;
    // Send a POST request to the server
    $("#loadingModal").show();
    $("#convertedTextOutput").hide();
    $("#convertedHtmlOutput").hide();
    $("#downloadButton").hide();
    $("#previewButton").hide();
    $("#image-container").show();

    var xhr = new XMLHttpRequest();
    xhr.timeout = 360000;

        xhr.open('POST', apiUrl + '/file-convert/upload', true);
    xhr.onerror = function() {
        alert("Network error");
        $("#loadingModal").hide();
    }
    ;
    xhr.onload = function() {
        if (xhr.status === 200) {
            convertedHtmlRaw = xhr.response;

            if (fileType === "docx") {
                convertedHtmlRaw = convertedHtmlRaw.replace("<body", "<style>body>* {width: min-content;min-width: 211mm;height: auto;margin:auto !important;padding: 40px 0px;margin-top: 20px!important;margin-bottom: 20px!important;background: white;}</style><body style=\"background:lightgrey;\"");
            }
            if (fileType === "doc") {
                convertedHtmlRaw = convertedHtmlRaw.replace("<body", "<style>body {margin: auto !important;width: 36% !important;min-width: 211mm;height: auto;margin: auto;margin-top: 20px !important;margin-bottom: 20px !important;background: white;}</style><body");
            }

            convertedHtmlRaw = convertedHtmlRaw.replace("height: 72%;", "height: 93%;");

            $("#convertedTextOutput")[0].innerText = convertedHtmlRaw;
            $("#convertedHtmlOutput")[0].innerHTML = xhr.response;       

            $("#convertedTextOutput").show();
            $("#convertedHtmlOutput").hide();
            $("#downloadButton").show();
            $("#previewButton").show();
            $("#image-container").hide();

        } else {
            alert('Error converting the file. Please try again.');
        }
        $("#loadingModal").hide();
    }
    ;
    xhr.send(formData);
}

function downloadFile() {
    var link = document.createElement('a');
    link.href = 'data:text/html;charset=utf-8,' + encodeURIComponent(convertedHtmlRaw);
    link.download = convertedFileName + '.html';
    link.click();
}

function previewFile() {
    if ($("#convertedTextOutput").css('display') != 'none') {
        $("#convertedHtmlOutput").show();
        $("#convertedTextOutput").hide();
    } else {
        $("#convertedHtmlOutput").hide();
        $("#convertedTextOutput").show();
    }
}
function showTab(id,event){
    var allTable = document.getElementsByClassName("excelDefaults")
    var x = document.getElementById(id);
    for (const element of allTable) {
        element.style.display = "none"
    }
    x.style.display = "block"

    const tabs = document.getElementById("tabs");
    for (const child of tabs.children) {
        child.className =''
    }
    event.target.classList.add('on')
}

document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.getElementById("tabs");
    if(tabs != null)
    tabs.children[0].click()
});