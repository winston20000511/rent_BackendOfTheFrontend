/* 上排搜尋條件的按鈕 */
const conditionBtns = document.querySelectorAll(".condition-btn");

const allOrdersBtn = document.getElementById("all-orders-btn");
const aliveOrdersBtn = document.getElementById("alive-orders-btn");
const canceledOrdersBtn = document.getElementById("canceled-orders-btn");
const menuBtn = document.getElementById("menu-button");

/* 使用者輸入搜尋條件的輸入框 */
const searchInput = document.getElementById("search-input");

/* 搜尋條件 - 下拉式選單選項 */
const menuItems = document.querySelectorAll(".menu-item");
const menuItemMerchantTradNo = document.getElementById("menu-item-marchantTradNo");
const menuItemDatePicker = document.getElementById("menu-item-datetimepicker");
const menuItemHouseTitle = document.getElementById("menu-item-houseTitle");

/* 處理下單選單 - 以其他方式搜尋 */
const dropdownButton = document.getElementById("menu-button");
const dropdownMenu = document.getElementById("dropdown-menu");

dropdownButton.addEventListener("click", function (event) {
  event.stopPropagation();
  dropdownMenu.classList.toggle("hidden");
});

document.addEventListener("click", function (event) {
  if (
    !dropdownButton.contains(event.target) &&
    event.target != dropdownButton
  ) {
    dropdownMenu.classList.add("hidden");
  }
});

/* 讀取上排按鈕們 */
conditionBtns.forEach((button) => {
  button.addEventListener("click", function () {
    conditionBtns.forEach((btn) => btn.classList.remove("bg-blue-700"));

    conditionBtns.forEach((btn) => btn.classList.add("bg-blue-500"));

    this.classList.add("bg-blue-700");

    if(this != menuBtn){
        menuBtn.innerHTML='其他搜尋方式 <i class="fa-solid fa-arrow-down py-1"></i>';
        searchInput.classList.add("hidden");
    }
  });
});

/* 處理下拉選單的細部操作 */
dropdownMenu.addEventListener("click", function (event) {
    event.stopPropagation();

    dropdownMenu.classList.add("hidden");

    if(searchInput.id == "date-input"){
        searchInput.id="search-input";
        searchInput._flatpickr.destroy();
    }

    if(event.target.closest("#menu-item-marchantTradNo")){
        getMerchantTradeInput(event);
    }

    if(event.target.closest("#menu-item-datetimepicker")){
        getDateTimeInput(event);
    }

    if(event.target.closest("#menu-item-houseTitle")){
        getHouseTitleInput(event);
    }

});


function getMerchantTradeInput(event){
    if (!event.target.contains(dropdownMenu)) {
        if (event.target.contains(menuItemMerchantTradNo)) {
          menuBtn.innerHTML =
            '以訂單號碼搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';
    
          searchInput.classList.remove("hidden");
    
          searchInput.placeholder = "請輸入訂單編號";
        }
    }
}

function getDateTimeInput(event){
    if (event.target.contains(menuItemDatePicker)) {
        menuBtn.innerHTML =
          '以時間搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';
  
        searchInput.classList.remove("hidden");
  
        searchInput.id = "date-input";
  
        searchInput.placeholder = "請選擇想查詢的時間區段";
  
        let searchInputValue = "";
  
        function getSearchInputValue(){
          return new Promise((response) => {
              flatpickr("#date-input", {
                  mode: "range",
                  time_24hr: true,
                  dateFormat: "Y-m-d",
                  onChange: function (period) {
                    if(period.length==2){
                      const startDate = period[0];
                      const endDate = period[1];
  
                      searchInputValue =`${startDate.toISOString()} to ${endDate.toISOString()}}`;
  
                      response(searchInputValue);
                    }
                  },
                });
          });
        }
  
        getSearchInputValue().then(response =>{
          const jsonString = JSON.stringify(response);
          /* jsonString:
          2024-11-15T16:00:00.000Z to 2024-11-16T16:00:00.000Z} */
        });     
  
    }
}

function getHouseTitleInput(event){
    if (event.target.contains(menuItemHouseTitle)) {
        menuBtn.innerHTML =
        '以房屋標題搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

      searchInput.classList.remove("hidden");

      searchInput.placeholder = "請輸入房屋標題";
    }
}


/* 訂單內容 modal */
const checkDetailBtn = document.getElementById("check-detail-btn");
const modal = document.getElementById("modal");
const modalBackground = document.getElementById("modal-background");

checkDetailBtn.addEventListener("click", function(){
  modal.classList.remove("hidden");
  toggleModalBackground();
});

modal.addEventListener("click", function(event){
  if(event.target.id === "close-modal-btn"){
    modal.classList.add("hidden");
    toggleModalBackground();
  }
});

function toggleModalBackground(){
  console.log(modalBackground.classList)
  if(modalBackground.classList.contains("hidden")){
    modalBackground.classList.add("overlay");
    modalBackground.classList.remove("hidden");
  }else if(!modalBackground.classList.contains("hidden")){
    modalBackground.classList.remove("overlay");
    modalBackground.classList.add("hidden");
  }
}
