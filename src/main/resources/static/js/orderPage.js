
/* 初始化 table */
initDataTable();

function initDataTable(){
  getAllOrders();
}


/* 上排搜尋條件的按鈕 */
const conditionBtnsGroup1 = Array.from(document.querySelectorAll(".condition-btn-group1"));
const conditionBtnsGroup2 = Array.from(document.querySelectorAll(".condition-btn-group2"));
const dropdownMenuItems = Array.from(document.querySelectorAll(".menu-item"));
const menuBtn = document.getElementById("menu-button");


/* 使用者輸入搜尋條件的輸入框 */
const searchInput = document.getElementById("search-input");

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

const selectedConditions = {
  status: "allAds",
  condition: null,
  input: null,
}

// 初始化監聽事件
initListeners();

function initListeners(){
  initBtnListeners(conditionBtnsGroup1, "group1");
  initBtnListeners(conditionBtnsGroup2, "group2");
  initDropMenuListeners(dropdownMenuItems);
}

function initBtnListeners(buttons, group){
  buttons.forEach((button) =>{
    button.addEventListener("click", function(){
      handleButtonSelection(this, group);
      applyFilters();
    });
  });
}

function initDropMenuListeners(menuItems){
  menuItems.forEach((item) =>{
    item.addEventListener("click", function(){
      const searchCondition = item.value;
      handleDropdownSelection(searchCondition);
    });
  });
}

function handleButtonSelection(button, group){
  let buttons = group === "group1"? conditionBtnsGroup1 : conditionBtnsGroup2;

  removeBtnColor(buttons);

  if(group === "group1"){
    selectedConditions.status = button.value;
  }
  
  button.classList.add("bg-blue-700");
}

// 按鈕除色
function removeBtnColor(buttons) {
  buttons.forEach((button) => {
    button.classList.remove("bg-blue-700");
    button.classList.add("bg-blue-500");
  });
}

function handleDropdownSelection(condition){
  showInputElements(condition);
  selectedConditions.condition = condition;

  searchInput.removeEventListener("input", handleInputChange);

  searchInput.addEventListener("input", handleInputChange);
}

function handleInputChange(event) {
  event.stopPropagation();
  const input = event.target.value;
  selectedConditions.input = input;
  // console.log("handleDropdownSelection: ", selectedConditions);
}

function applyFilters(){
  // const {status, condition, input} = selectedConditions;

  console.log("filter: ", selectedConditions);

  filterOrders(selectedConditions);
}

// 取得所有訂單的資料
function getAllOrders() {

  // 測試資料
  let pageNumber = 1;

    fetch(
      `http://localhost:8080/api/orders/${pageNumber}`,
      { 
        method: "GET",
      }
    ).then((response) => {
      return response.json();
    }).then((orders) =>{

      /* {userId, merchantId, merchantTradNo, merchantTradDate, totalAmount, tradeDesc, itemName, orderStatus, returnUrl, choosePayment, checkMacValue} */

      if(orders.length === 0){
        const tbody = document.querySelector("#paid-list-table tbody");
        tbody.innerHTML = "";
        row = 
          `<tbody>
            <tr>
              <td class="px-4 py-2">查無資料</td>
            </tr>
          </tbody>`;
          tbody.innerHTML += row;

        return;
      }

      const tbody = document.querySelector("#paid-list-table tbody");
      tbody.innerHTML = "";

      for (let i = 0; i < orders.length; i++) {

        // 之後放去DTO計算
        const orderDate = orders[0].merchantTradDate.substring(0,10);
        const orderStatus = orders[0].orderStatus === 0? "已取消" : "一般訂單";

        row = 
        `<tbody>
          <tr>
            <td class="px-4 py-2">${orders[i].merchantTradNo}</td>
            <td class="px-4 py-2">
              <ul>
                <li>動態新增訂單內容物</li>
                <li>至多兩物件</li>
              </ul>
            </td>
            <td class="px-4 py-2">${orderDate}</td>
            <td class="px-4 py-2">${orderStatus}</td>
            <td class="px-4 py-2 text-center">
              <button
                  id = "check-detail-btn"
                  type="button"
                  class="bg-blue-500 rounded text-white w-16 py-2 hover:bg-blue-300">
                  查看
              </button></td>
          </tr>
        </tbody>`;
        tbody.innerHTML += row;
      }
    })
    .catch((error) =>{
      console.log(error);
    });
}

// 以訂單狀態搜尋
function filterOrders(){

  // const selectedConditions = {
  //   status: "allAds",
  //   condition: null,
  //   input: null,
  // }

  // 搜尋送資料有問題
  
  const selectedConditionsJson = JSON.stringify(selectedConditions);
  
  // 測試資料
  // const pageNumber = 1;
  // const searchCondition = {
  //   "userId" : "1",
  //   "status" : "active",
  //   "merchantTradNo" : null,
  //   "startDate" : null,
  //   "endDate" : null,
  //   "houseTitle" : "租屋網@1"
  // }
  // const searchConditionJson = JSON.stringify(searchCondition);

  fetch("http://localhost:8080/api/orders/filter",{
    method: "POST",
    headers: {
      'Content-Type': 'application/json',
    },
    body: selectedConditionsJson,
  })
  .then((response) =>{
    return response.json();
  })
  .then((json) =>{
    console.log(json);
  })
  .catch((error) =>{
    console.log(error);
  });
  
}


/* 下拉選單選項 */
function showInputElements(condition){

  if (searchInput.flatpickrInstance) {
    searchInput.flatpickrInstance.destroy();
    delete searchInput.flatpickrInstance;
  }

  if(condition === "merchantTradNo"){
    menuBtn.innerHTML =
    '以訂單號碼搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.placeholder = "請輸入訂單編號";
  }

  if(condition === "period"){
    menuBtn.innerHTML =
    '以時間搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.id = "date-input";

    searchInput.placeholder = "請選擇想查詢的時間區段";

    const flatpickrInstance = flatpickr("#date-input", {
      mode: "range",
      time_24hr: true,
      dateFormat: "Y-m-d",
      onChange: function (period) {
        if (period.length === 2) {
          const startDate = period[0];
          const endDate = period[1];
          const searchInputValue = `${startDate.toISOString()} to ${endDate.toISOString()}`;
          console.log("Selected range:", searchInputValue);
        }
      },
    });

    searchInput.flatpickrInstance = flatpickrInstance;

  }

  if(condition === "houseTitle"){
    menuBtn.innerHTML =
    '以房屋標題搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.placeholder = "請輸入房屋標題";
  }

  if(condition === "none"){
    menuBtn.innerHTML =
    '不設條件 <i class="fa-solid fa-arrow-down py-1"></i>';
    searchInput.classList.add("hidden");
  }
}



/* 訂單內容 modal */
/*
const checkDetailBtn = document.getElementById("check-detail-btn");
const modal = document.getElementById("modal");
const modalBackground = document.getElementById("modal-background");

checkDetailBtn.addEventListener("click", function () {
  modal.classList.remove("hidden");
  toggleModalBackground();
});

modal.addEventListener("click", function (event) {
  if (event.target.id === "close-modal-btn") {
    modal.classList.add("hidden");
    toggleModalBackground();
  }
});

function toggleModalBackground() {
  modalBackground.classList.toggle("overlay");
  modalBackground.classList.toggle("hidden");
}
*/
