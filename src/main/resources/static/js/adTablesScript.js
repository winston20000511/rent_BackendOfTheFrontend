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
export function clickConditionBtn(onButtonClick) {
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
  Object.values(tables).forEach((table) => {
    table.classList.add("hidden");
  });

  if (clickedBtn === paidAdsBtn) {
    paidAdTable.classList.remove("hidden");
  }

  if (clickedBtn === unPaidAdsBtn) {
    unPaidAdTable.classList.remove("hidden");
  }

  if (clickedBtn === addAdBtn) {
    addAdTable.classList.remove("hidden");
  }

}

/* 設定表格事件 */
// 訂單詳細 Modal
export function clickAdDetailBtn(onDetailButtonClick) {

  Object.values(tables).forEach((table) => {
    table.addEventListener("click", function (event) {
      event.stopPropagation();
      const clickedBtn = event.target.closest(".check-detail-btn");

      if (clickedBtn) {
        const row = clickedBtn.closest("tr");
        // const adId = row.querySelector(".ad-id").textContent;
         const adDetails = {
           adId: 1,
           title: "假資料"
         };

        if(onDetailButtonClick){
            onDetailButtonClick(adDetails);
        }
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


/* 塞資料進表格 */
