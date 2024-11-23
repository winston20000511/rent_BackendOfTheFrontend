/* 上方搜尋及新增廣告按鈕 */
const conditionBtns = document.querySelectorAll(".condition-btn");
const paidAdsBtn = document.getElementById("paid-ads-btn");
const unPaidAdsBtn = document.getElementById("unpaid-ads-btn");
const addAdBtn = document.getElementById("add-ad-btn");

/* 表格們 */
const paidAdTable = document.getElementById("paid-ads-table");
const unPaidAdTable = document.getElementById("unpaid-ads-table");
const addAdTable = document.getElementById("add-ad-table");

/* 輸出選定的DOM元素 */
const tables = { paidAdTable, unPaidAdTable, addAdTable };
const conditionButtons = { paidAdsBtn, unPaidAdsBtn, addAdBtn };

/* 按鈕互動 */
export function getclickedConditionBtn(onButtonClick) {
  conditionBtns.forEach((button) => {
    button.addEventListener("click", function () {
      updateButtonStyles(this);
      onButtonClick(this);
    });
  });
}

// 全部恢復 blue-500 再讓點選到的按鈕 blue-700
function updateButtonStyles(clickedBtn) {
  conditionBtns.forEach((button) => {
    button.classList.remove("bg-blue-700");
    button.classList.add("bg-blue-500");
  });

  clickedBtn.classList.add("bg-blue-700");
}

/* 表格 */
// 初始化表格
export function initTable() {
    const defualtBtn = paidAdsBtn;
    updateButtonStyles(defualtBtn);
    showSelectedTable(defualtBtn);
    // 抓資料放進來
}

// 顯示選定的表格
export function showSelectedTable(clickedBtn) {

  // 測試用的預設值
  let userId = 1;
  let isPaid = true;
  let pageNumber = 1;

  Object.values(tables).forEach((table) => {
    table.classList.add("hidden");
  });

  if (clickedBtn === paidAdsBtn) {
    paidAdTable.classList.remove("hidden");
    isPaid = true;
    // userId = 5; //測試資料
    fetch(`http://localhost:8080/advertisements/tabledata/${userId}/${isPaid}/${pageNumber}`,{
      method: "GET"
    }).then(response=>{
      return response.json();
    }). then(tableDetails =>{

      const tbody = document.querySelector("#paid-ads-table tbody");

      tbody.innerHTML = '';

      if(tableDetails.length === 0){

        const div = document.createElement("div");
        div.classList.add("px-4", "py-2");
        div.textContent = "查無資料";

        tbody.appendChild(div);

        return;
      }

      tableDetails.forEach(rowdata=>{

        /*
        const{adId, adName, adPrice, houseTitle, isPaid, orderId, paidDate, userId, userName} = tableDetails[0];
        */

        const rowdataHTML =
        `
          <tr>
            <td class="ad-id px-4 py-2">${rowdata.adId}</td>
            <td class="text-left">
              <a class="house-title-link underline" href="#">${rowdata.houseTitle}</a>
            </td>
            <td class="px-4 py-2 flex justify-center text-center">
              <button
                type="button"
                class="check-detail-btn bg-blue-500 rounded text-white w-16 py-2 hover:bg-blue-300">
                查看
              </button>
            </td>
          </tr>`; 

          tbody.innerHTML += rowdataHTML;
      });

    }).catch(error=>{
      console.log(error);
    });
  }

  if (clickedBtn === unPaidAdsBtn) {
    unPaidAdTable.classList.remove("hidden");
    isPaid = false;

    // 測試資料
    userId = 50;

    fetch(`http://localhost:8080/advertisements/tabledata/${userId}/${isPaid}/${pageNumber}`,{
      method: "GET"
    }).then(response=>{
      return response.json();
    }). then(tableDetails =>{

      const tbody = document.querySelector("#unpaid-ads-table tbody");

      tbody.innerHTML = '';

      if(tableDetails.length === 0){

        const div = document.createElement("div");
        div.classList.add("px-4", "py-2");
        div.textContent = "查無資料";

        tbody.appendChild(div);

        return;
      }

      let totalAmount = 0;
      tableDetails.forEach(rowdata=>{
        const rowdataHTML =
        `
          <tr>
            <td class="ad-id px-4 py-2">${rowdata.adId}</td>
            <td>
              <a class="house-title-link underline" href="#">${rowdata.houseTitle}</a>
            </td>
            <td class="ad-price px-4 py-2 text-right">${rowdata.adPrice}</td>
            <td class="px-4 py-2 flex justify-center text-center">
              <button
                type="button"
                class="check-detail-btn bg-blue-500 rounded text-white w-16 py-2 hover:bg-blue-300"
              >
                查看
              </button>
            </td>
            <td class="px-4 py-2 text-center"><input type="checkbox" /></td>
          </tr>`;
          
      totalAmount += rowdata.adPrice;

      tbody.innerHTML += rowdataHTML;

    });

    const totalAmountHTML = 
    `
      <tr>
        <td class="px-4 py-2 text-right border-t border-gray-400" colspan="2">
          總計
        </td>
        <td id="total-amount" class="px-4 py-2 text-right border-t border-gray-400">
          ${totalAmount}
        </td>
        <td class="px-10 border-t border-gray-400 text-right" colspan="2">
          <a id="buy-link" href="#">
          前往下單
          <i class="fa-solid fa-arrow-right"></i>
          </a>
        </td>
      </tr>
    `

    tbody.innerHTML += totalAmountHTML;
      
    }).catch(error=>{
      console.log(error);
    });

  }

  if (clickedBtn === addAdBtn) {
    addAdTable.classList.remove("hidden");
    isPaid = false;
    userId = 5; //測試資料
    fetch(`http://localhost:8080/advertisements/tabledata/${userId}/${isPaid}/${pageNumber}`,{
      method: "GET"
    }).then(response=>{
      return response.json();
    }). then(tableDetails =>{
      console.log(tableDetails);

      const tbody = document.querySelector("#add-ad-table tbody");

      tbody.innerHTML = '';

      if(tableDetails.length === 0){

        const div = document.createElement("div");
        div.classList.add("px-4", "py-2");
        div.textContent = "查無資料";

        tbody.appendChild(div);

        return;
      }

      let totalAmount = 0;
      tableDetails.forEach(rowdata=>{

        const rowdataHTML = 
      `
        <tr>
          <td class="px-4 py-2">${rowdata.adId}</td>
          <td class="px-4 py-2 underline">
            <a href="#">${rowdata.houseTitle}</a>
          </td>
          <td class="px-4 py-2">
            <select name="ad-duration" id="ad-duration">
              <option>30天</option>
              <option>60天</option>
            </select>
          </td>
          <td class="px-4 py-2 text-right">300</td>
          <td class="px-4 py-2 text-center"><input type="checkbox" /></td>
        </tr>
        <tr>
          <td
            class="text-right border border-t border-gray-300"
            colspan="5"
          >
            <button
              class="confirm-add-btn bg-blue-500 text-white py-2 px-4 rounded m-2 hover:bg-blue-300"
            >
              確認新增廣告
            </button>
          </td>
        </tr>`;

        tbody.innerHTML += rowdataHTML;

        const selectElement = tbody.querySelector("select[name='ad-duration']");

        console.log(selectElement); //OK

        // 將靜態<option>刪除
        // fetch 取得天數

      });

    }).catch(error=>{
      console.log(error);
    });
  }


}

/* 設定表格事件 */
// 訂單詳細 Modal
export function getDetailByclickAdDetailBtn(onDetailButtonClick) {

  Object.values(tables).forEach((table) => {
    table.addEventListener("click", function (event) {
      event.stopPropagation();
      const clickedBtn = event.target.closest(".check-detail-btn");

      if (clickedBtn) {
        const row = clickedBtn.closest("tr");
        const adId = row.querySelector(".ad-id").textContent;
        
        console.log(adId);

        // 將adId傳給server
        fetch(`http://localhost:8080/advertisements/details/${adId}`,{
          method:"GET",
        })
        .then(response =>{
          return response.json();
        })
        .then(adDetails =>{
          if(onDetailButtonClick){
            onDetailButtonClick(adDetails);
          }else{
            console.log("沒有callback: onDetailButtonClick");
          }
        })
        .catch(error =>{
          console.log("fetch error: " + error);
        });

      }
    });
  });
}

// 顯示成功的Modal
export function clickConfirmBtn(onclickConfirmButtonClick) {

  Object.values(tables).forEach((table) => {
    table.addEventListener("click", function (event) {
      event.stopPropagation();
      const clickedBtn = event.target.closest(".confirm-add-btn");

      if(clickedBtn) {
        onclickConfirmButtonClick();
      }
    });
  });
}


/* 向後端請求資料 */
// 1. 求廣告表格們的資料

// function fetchTableDataByConditions(isPaid, pageNumber){
//   fetch(`http://localhost:8080/advertisements/${userId}/${isPaid}/${pageNumber}`,{
//     method: "GET"
//   }).then(response=>{
//     return response.json();
//   }). then(tableDetails =>{
//     console.log(tableDetails);

//     // 顯示已付款廣告

//     // 顯示未付款廣告

//     // 顯示新增廣告

//   }).catch(error=>{
//     console.log(error);
//   });
// }

// 2. 求廣告詳細資料(寫在Modal中)
