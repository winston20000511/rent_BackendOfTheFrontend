/* 上方搜尋及新增廣告按鈕 */
const conditionBtns = document.querySelectorAll(".condition-btn");

const paidAdsBtn = document.getElementById("paid-ads-btn");
const unPaidAdsBtn = document.getElementById("unpaid-ads-btn");
const addAdBtn = document.getElementById("add-ad-btn");

/* 表格們 */
const paidAdTable = document.getElementById("paid-ads-table");
const unPaidAdTable = document.getElementById("unpaid-ads-table");
const addAdTable = document.getElementById("add-ad-table");

conditionBtns.forEach(button=>{
    button.addEventListener("click", function(){
        conditionBtns.forEach(btn=>btn.classList.remove("bg-blue-700"));

        conditionBtns.forEach(btn=>btn.classList.add("bg-blue-500"));

        this.classList.add("bg-blue-700");

        /* 當選到已付款廣告 > 列出所有已付款廣告 */
        if(this==paidAdsBtn){
            paidAdTable.classList.remove("hidden");
            unPaidAdTable.classList.add("hidden");
            addAdTable.classList.add("hidden");
        }

        if(this==unPaidAdsBtn){
            paidAdTable.classList.add("hidden");
            unPaidAdTable.classList.remove("hidden");
            addAdTable.classList.add("hidden");
        }

        if(this==addAdBtn){
            paidAdTable.classList.add("hidden");
            unPaidAdTable.classList.add("hidden");
            addAdTable.classList.remove("hidden");
        }
    });
});

/* 成功新增廣告提示窗 */
const succeessModal = document.getElementById("success-modal");
const modalBackground = document.getElementById("modal-background");

addAdTable.addEventListener("click", function(event){

    if(event.target.classList.contains("confirm-add-btn")){
        succeessModal.classList.remove("hidden");
        toggleModalBackground();
    }

});
   
document.addEventListener("click", function(event){
    event.stopPropagation();
    if(!succeessModal.classList.contains("hidden")){

        if(succeessModal.contains(event.target) && event.target.id != "check-unpaid-ads"){
            succeessModal.classList.add("hidden");
            toggleModalBackground();
        }

        if(event.target.id === "check-unpaid-ads"){
            succeessModal.classList.add("hidden");
            addAdTable.classList.add("hidden");
            unPaidAdTable.classList.remove("hidden");
            addAdBtn.classList.remove("bg-blue-700");
            addAdBtn.classList.add("bg-blue-500");
            unPaidAdsBtn.classList.add("bg-blue-700");
            toggleModalBackground();
        }
    }
});

function toggleModalBackground(){
    if(modalBackground.classList.contains("hidden")){
        modalBackground.classList.add("overlay");
        modalBackground.classList.remove("hidden");
    }else if(!modalBackground.classList.contains("hidden")){
        modalBackground.classList.remove("overlay");
        modalBackground.classList.add("hidden");
    }
}



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

/* 廣告詳細內容的彈跳視窗 */
const adDetailModal = document.getElementById("ad-detail-modal");

paidAdTable.addEventListener("click", function(event){
    toggleAdModal(event);
});


unPaidAdTable.addEventListener("click", function(event){
    toggleAdModal(event);
});

function toggleAdModal(event){
    event.stopPropagation();
    if(event.target.classList.contains("check-detail-btn")){
        adDetailModal.classList.remove("hidden");
        toggleModalBackground();
    }

    adDetailModal.addEventListener("click", function(event){
        event.stopPropagation();
        if(event.target.id === "close-ad-modal-btn"){
            adDetailModal.classList.add("hidden");
            toggleModalBackground();
        }
    });
}