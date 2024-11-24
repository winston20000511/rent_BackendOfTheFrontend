import * as adTablesJS from './adTablesScript.js';
// ad modals: ad details + prompt success
import * as adModalsJS from './adModalsScript.js';

adTablesJS.initTable();

// 點擊選項按鈕，顯示選取的資料表格
adTablesJS.getclickedConditionBtn(function(clickedBtn){
    adTablesJS.showSelectedTable(clickedBtn);
});

// 點擊「查看」按鈕時，顯示選取的廣告內容
adTablesJS.getDetailByclickAdDetailBtn(function(adDetails){
    adModalsJS.showAdDetailsModal(adDetails);
});


// 成功新增廣告提示窗
adTablesJS.clickConfirmBtn(function(){
    adModalsJS.showSuccessAddAdsModal();
});

adModalsJS.clickModalBtn(function(clickedBtn){
    if(clickedBtn.id === "close-sucess-modal"){
        adModalsJS.closeModalBtn();
    }
});