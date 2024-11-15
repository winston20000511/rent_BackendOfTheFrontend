/* 上方搜尋及新增廣告按鈕 */
const conditionBtns = document.querySelectorAll(".condition-btn");

const paidAdsBtn = document.getElementById("paid-ads-btn");
const unPaidAdsBtn = document.getElementById("unpaid-ads-btn");
const addAdBtn = document.getElementById("add-ad-btn");

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
            console.log(this);
            paidAdTable.classList.add("hidden");
            unPaidAdTable.classList.add("hidden");
            addAdTable.classList.remove("hidden");
        }
    });
});


/* 處理下單選單 - 以其他方式搜尋 */
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

