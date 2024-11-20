import * as adTablesJS from './adTablesScript.js';
// ad modals: ad details + prompt success
import * as adModalsJS from './adModalsScript.js';

adTablesJS.initTable();

// 點擊選項按鈕，顯示選取的資料表格
adTablesJS.clickConditionBtn(function(clickedBtn){
    adTablesJS.showSelectedTable(clickedBtn);
});

// 點擊「查看」按鈕時，顯示選取的廣告內容
adTablesJS.clickAdDetailBtn(function(adDetails){
    adModalsJS.showAdDetailsModal(adDetails);
});


/* 成功新增廣告提示窗 */
adTablesJS.clickConfirmBtn(function(){
    adModalsJS.showSuccessAddAdsModal();
});

adModalsJS.clickModalBtn(function(clickedBtn){

    if(clickedBtn.id === "close-sucess-modal"){
        adModalsJS.closeModalBtn();
    }

    /*
    if(clickedBtn.id === "stay-btn"){
        adModalsJS.stayAtAddAd();
    }
    
    if(clickedBtn.id === "check-unpaid-ads"){
        adModalsJS.goCheckUnpaidAds();
    }
    */
});


/* 處理下單選單 - 以其他方式搜尋 */
/*
const dropdownButton = document.getElementById("menu-button");
const dropdownMenu = document.getElementById("dropdown-menu");

dropdownButton.addEventListener("click", function(event){
    event.stopPropagation();
    dropdownMenu.classList.toggle("hidden");
});

document.addEventListener("click", function(event){
    if(!dropdownButton.contains(event.target) && event.target != dropdownButton){
        dropdownMenu.classList.add("hidden");
    }
});
*/