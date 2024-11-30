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
    
    //測試資料
    const searchCondition = {"userId" : userId, "isPaid" : isPaid};
    const searchConditionJson = JSON.stringify(searchCondition);

    fetch(
      `http://localhost:8080/advertisements/search`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: searchConditionJson,
      }
    )
      .then((response) => {
        return response.json();
      })
      .then((tableDetails) => {
        const tbody = document.querySelector("#paid-ads-table tbody");

        tbody.innerHTML = "";

        if (tableDetails.length === 0) {
          const div = document.createElement("div");
          div.classList.add("px-4", "py-2");
          div.textContent = "查無資料";

          tbody.appendChild(div);

          return;
        }

        tableDetails.forEach((rowdata) => {
          /*
          const{adId, adName, adPrice, houseTitle, isPaid, orderId, paidDate, userId, userName} = tableDetails[0];
          */

          const rowdataHTML = `
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
      })
      .catch((error) => {
        console.log(error);
      });
  }

  if (clickedBtn === unPaidAdsBtn) {
    unPaidAdTable.classList.remove("hidden");
    isPaid = false;

    // 測試資料
    userId = 50;
    const searchCondition = {"userId" : userId, "isPaid" : isPaid};
    const searchConditionJson = JSON.stringify(searchCondition);

    fetch(
      `http://localhost:8080/advertisements/search`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: searchConditionJson,
      }
    )
      .then((response) => {
        return response.json();
      })
      .then((tableDetails) => {
        const tbody = document.querySelector("#unpaid-ads-table tbody");

        tbody.innerHTML = "";

        if (tableDetails.length === 0) {
          const div = document.createElement("div");
          div.classList.add("px-4", "py-2");
          div.textContent = "查無資料";

          tbody.appendChild(div);

          return;
        }

        tableDetails.forEach((rowdata) => {
          const rowdataHTML = `
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

          tbody.innerHTML += rowdataHTML;
        });

        const checkBoxesElements = document.querySelectorAll(
          "#unpaid-ads-table input[type='checkbox']"
        );
        let totalAmount = 0;

        checkBoxesElements.forEach((checkbox) => {
          checkbox.addEventListener("change", function () {
            getCheckedBoxes();
            totalAmount = getTotalAmonunt();

            let totalAmountRow = document.getElementById("total-amount-row");

            if (getCheckedBoxes().length > 0) {
              if (totalAmountRow) {
                totalAmountRow.querySelector("#total-amount").textContent =
                  totalAmount;
              } else {
                const totalAmountHTML = `
              <tr id="total-amount-row">
                <td class="px-4 py-2 text-right border-t border-gray-400" colspan="2">
                  總計
                </td>
                <td id="total-amount" class="px-4 py-2 text-right border-t border-gray-400">
                  ${totalAmount}
                </td>
                <td class="px-10 border-t border-gray-400 text-right" colspan="2">
                  <a id="buy-link" href="http://localhost:8080/orders/confirmpage">
                  前往下單
                  <i class="fa-solid fa-arrow-right"></i>
                  </a>
                </td>
              </tr>
            `;
                tbody.insertAdjacentHTML("beforeend", totalAmountHTML);
              }
            } else {
              if (totalAmountRow) {
                totalAmountRow.remove();
              }
            }

            const adIds = getCheckedBoxes().map((checked) => checked.adId);

            // 如何將選擇的ads送到下一頁??
          });
        });

        function getCheckedBoxes() {
          let check = [];
          checkBoxesElements.forEach((checkbox) => {
            if (checkbox.checked) {
              const row = checkbox.closest("tr");
              const adId = row.querySelector(".ad-id").textContent;
              const adPrice = row.querySelector(".ad-price").textContent;
              check.push({ adId: adId, adPrice: adPrice });
            }
          });

          return check;
        }

        // 取得所選廣告的總價
        function getTotalAmonunt() {
          const checkedBoxes = getCheckedBoxes();
          let total = 0;

          checkedBoxes.forEach((box) => {
            total += parseInt(box.adPrice) || 0;
          });

          return total;
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }

  if (clickedBtn === addAdBtn) {
    addAdTable.classList.remove("hidden");

    userId = 54; //測試資料
    fetch(`http://localhost:8080/advertisements/noadhouses/${userId}`, {
      method: "GET",
    })
      .then((response) => {
        return response.json();
      })
      .then((tableDetails) => {
        const tbody = document.querySelector("#add-ad-table tbody");

        tbody.innerHTML = "";

        if (tableDetails.length === 0) {
          const div = document.createElement("div");
          div.classList.add("px-4", "py-2");
          div.textContent = "查無資料";

          tbody.appendChild(div);

          return;
        }

        tableDetails.forEach((rowdata) => {
          const rowdataHTML = `  
            <tr>
              <td class="house-id px-4 py-2">
                ${rowdata.houseId}
              </td>
              <td class="px-4 py-2 underline">
                <a href="#">${rowdata.houseTitle}</a>
              </td>
              <td class="px-4 py-2">
                <select name="ad-duration" class="ad-duration"></select>
              </td>
              <td class="selected-ad-price px-4 py-2 text-right"></td>
              <td class="px-4 py-2 text-center">
                <input type="checkbox" />
              </td>
            </tr>`;

          tbody.innerHTML += rowdataHTML;
        });

        const selectElements = tbody.querySelectorAll(".ad-duration");

        let adtypes = [];
        let adData = [];
        let selectedAdDataMap = new Map();

        fetch("http://localhost:8080/advertisements/adtype", {
          method: "GET",
        })
          .then((response) => {
            return response.json();
          })
          .then((json) => {
            const adJsonArray = JSON.parse(JSON.stringify(json));
            adJsonArray.forEach((data) => {
              /* adName, adPrice, adTypeId*/
              adData.push(data);
              adtypes.push(data.adName);
            });

            selectElements.forEach((selectElement) => {
              adtypes.forEach((adtype) => {
                const optionElement = document.createElement("option");
                optionElement.textContent = adtype;
                optionElement.value = adtype;
                selectElement.appendChild(optionElement);
              });

              const row = selectElement.closest("tr");

              const adPriceElement = row.querySelector(".selected-ad-price");

              adPriceElement.textContent = adData[0].adPrice;

              selectElement.addEventListener("change", function () {
                const adName =
                  selectElement.options[selectElement.options.selectedIndex]
                    .value;

                const adPrice =
                  adData.find((item) => item.adName === adName)?.adPrice || 0;
                adPriceElement.textContent = adPrice;

                const houseId = parseInt(
                  row.querySelector(".house-id").textContent
                );

                if (selectedAdDataMap.has(houseId)) {
                  selectedAdDataMap.set(houseId, {
                    userId: userId,
                    houseId: houseId,
                    adName: adName,
                    adPrice: adPrice,
                  });
                }

                // console.log(
                //   "select改變時選取的當前物件: ",
                //   Array.from(selectedAdDataMap.values())
                // );
              });
            });

            tbody.addEventListener("change", function (event) {
              event.stopPropagation();

              const checkbox = event.target;

              if (event.target && event.target.type === "checkbox") {
                const row = checkbox.closest("tr");
                const houseId = parseInt(
                  row.querySelector(".house-id").textContent
                );

                if (checkbox.checked) {
                  const adName = row.querySelector("select").value;
                  const adPrice =
                    adData.find((item) => item.adName === adName)?.adPrice || 0;

                  selectedAdDataMap.set(houseId, {
                    userId: userId,
                    houseId: houseId,
                    adName: adName,
                    adPrice: adPrice,
                  });
                } else {
                  selectedAdDataMap.delete(houseId);
                }

                // 顯示確認行
                const checkedBoxes = tbody.querySelectorAll(
                  "#add-ad-table input[type='checkbox']"
                );
                const anyChecked = Array.from(checkedBoxes).some(
                  (checkbox) => checkbox.checked
                );
                const confirmRowHTML = document.getElementById("confirm-row");

                if (anyChecked) {
                  const confirmRow = `
                    <tr id="confirm-row">
                      <td class="text-right border border-t border-gray-300" colspan="5">
                        <button type="button" id="confirm-add-btn" class="bg-blue-500 text-white py-2 px-4 rounded m-2 hover:bg-blue-300">
                          確認新增廣告
                        </button>
                      </td>
                    </tr>`;

                  if (confirmRowHTML) {
                    confirmRowHTML.outerHTML = confirmRow;
                  } else {
                    tbody.insertAdjacentHTML("beforeend", confirmRow);
                  }
                } else {
                  if (confirmRowHTML) {
                    confirmRowHTML.remove();
                  }
                }
              }

              // console.log(
              //   "選取物件陣列: ",
              //   Array.from(selectedAdDataMap.values())
              // );
            });

            tbody.addEventListener("click", function (event) {
              if (event.target.id === "confirm-add-btn") {
                const selectedAdJson = JSON.stringify(
                  Array.from(selectedAdDataMap.values())
                );

                /*
                fetch("http://localhost:8080/advertisements/", {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: selectedAdJson,
                })
                  .then((response) => {
                    return response.json();
                  })
                  .then((isSuccess) => {
                    console.log(isSuccess);
                    if (isSuccess) {
                      // 確認完要把加好的house row刪掉
                      const removedHouseIds = [...selectedAdDataMap.keys()];

                      const houseIdTd = document.querySelectorAll(
                        "#add-ad-table .house-id"
                      );

                      houseIdTd.forEach((td) => {
                        const houseId = parseInt(td.textContent);
                        if (removedHouseIds.includes(houseId)) {
                          td.closest("tr").classList.add("hidden");
                        }
                      });
                    }
                  })
                  .catch((error) => {
                    console.log(error);
                  });
                  */

                }
            });
          })
          .catch((error) => {
            console.log(error);
          });
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

        // 將adId傳給server
        fetch(`http://localhost:8080/advertisements/details/${adId}`, {
          method: "GET",
        })
          .then((response) => {
            return response.json();
          })
          .then((adDetails) => {
            if (onDetailButtonClick) {
              onDetailButtonClick(adDetails);
            } else {
              console.log("沒有callback: onDetailButtonClick");
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    });
  });
}

// 點擊確認按鈕 => 顯示成功的Modal
export function clickConfirmBtn(onclickConfirmButtonClick) {
  Object.values(tables).forEach((table) => {
    table.addEventListener("click", function (event) {
      event.stopPropagation();
      const clickedBtn = event.target.closest("#confirm-add-btn");

      if (clickedBtn) {
        onclickConfirmButtonClick();
      }

    });
  });
}
