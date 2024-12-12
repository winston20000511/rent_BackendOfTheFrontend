document.addEventListener("DOMContentLoaded", async function () {
  const purchaseParamResponse = await fetch("/api/cart/getcartcontent");
  const purchaseParam = await purchaseParamResponse.json();
  // param = {"cartId": "", "paymentMethod": ""}

  // 取得廣告編號 + 房屋 + 方案 + 上架時間 + 金額
  const cartItemsResponse = await fetch("/api/orders/content/confirmation", {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify(purchaseParam.cartId),
  });

  const cartItems = await cartItemsResponse.json();

  console.log("cart items: ", cartItems);

  const tbody = document.querySelector("tbody");
  tbody.innerHTML = "";

  let totalAmount = 0;
  // const table內容
  cartItems.forEach((cartItem) => {
    tbody.innerHTML += generateCartItemRow(cartItem);
    totalAmount += cartItem.adPrice;
  });

  tbody.innerHTML += `<tr class="border-t border-gray-300">
      <td class="px-4 py-2 text-right font-bold" colspan="5">總計</td>
      <td class="px-4 py-2 text-right">${totalAmount}</td>
      <td class="px-4 py-2 text-center"></td>
    </tr>`;

  tbody.innerHTML += `<tr>
      <td colspan="6" class="text-right">
        <button type="button" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-400 transition duration-300">
          確認付款
        </button>
      </td>
    </tr>`;

  // 店選確認付款 > 呼叫ecpay API
  tbody.querySelector("button").addEventListener("click", async function(){
    console.log("click go to pay");

    const orderRequest = {"cartId" : purchaseParam.cartId, "paymentMethod" : purchaseParam.paymentMethod}

    // 建立訂單
    // const orderResponse = await fetch("/api/orders/create",{
    //   method: "POST",
    //   headers: {"content-type" : "application/json"},
    //   body: JSON.stringify(orderRequest)
    // });
    // const newOrder = await orderResponse.json();
    // console.log(newOrder);

    // 暫時先做跳轉
    // setTimeout(function() {
    //   window.location.href = "/orders/mylist";
    // }, 200);

    // 送ecpay付款
    // goToPay();
    try {
      const response = await fetch("/api/ecpay/test/ecpayCheckout", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        // body: JSON.stringify({cartId})
      });

      if (!response) {
        throw new Error("沒有回傳值");
      }

      const ecpayHTMLform = await response.text();
      const formContainer = document.getElementById("paymentFormContainer");formContainer.innerHTML = ecpayHTMLform;

      formContainer.querySelector('#allPayAPIForm').submit(); 

    } catch (error) {
      throw new Error(`發生錯誤: ${error.message}`);
    }
  });
});

function generateCartItemRow(cartItem) {
  const cartItemHTML = `<tr class="text-center">
    <td class="px-4 py-2">${cartItem.adId}</td>
    <td class="px-4 py-2">${cartItem.houseTitle}</td>
    <td class="px-4 py-2">${cartItem.adName}</td>
    <td class="px-4 py-2" colspan="2">${cartItem.adPeriod}</td>
    <td class="px-4 py-2 text-right">${cartItem.adPrice}</td>
    <td class="px-4 py-2"></td>
  </tr>`;

  return cartItemHTML;
}

async function goToPay(paymentMethod, cartId) {
  if (paymentMethod === "Credit") {
    try {
      const response = await fetch("/api/ecpay/test/ecpayCheckout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        // body: JSON.stringify({cartId})
      });

      if (!response) {
        throw new Error("沒有回傳值");
      }

      const ecpayHTMLform = await response.text();
      document.getElementById("paymentFormContainer").innerHTML = ecpayHTMLform;

    } catch (error) {
      throw new Error(`發生錯誤: ${error.message}`);
    }
  }

  if (paymentMethod === "linepay") {
  }
}
