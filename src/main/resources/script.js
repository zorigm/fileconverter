function showTab(id,event){
    let allTable = document.getElementsByClassName("excelDefaults");
    let x = document.getElementById(id);
    for (const element of allTable) {
        element.style.display = "none";
    }
    x.style.display = "block";

    const tabs = document.getElementById("tabs");
    for (const child of tabs.children) {
        child.className = '';
    }
    event.target.classList.add('on');
}

document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.getElementById("tabs");
    tabs.children[0].click();
});