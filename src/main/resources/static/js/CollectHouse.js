import HousePhoto from "./housePhoto.js";
const vueApp = {
  components: {
    EasyDataTable: window["vue3-easy-data-table"],
    HousePhoto,
  },
  data() {
    return {
      baseAddress: "http://localhost:8080",
      headers: [
        { text: "標題", value: "title", sortable: true, fixed: true },
        { text: "坪數", value: "size", sortable: true },
        { text: "地址", value: "Address", sortable: true },
        { text: "租金", value: "price", sortable: true },
        { text: "編輯", value: "operation" },
      ],
      houses: [],
      Model: {
        HouseId: null,
        title: null,
        price: null,
        size: null,
        Address: null,
        washingMachine: null,
        airConditioner: null,
        network: null,
        bedstead: null,
        mattress: null,
        refrigerator: null,
        ewaterHeater: null,
        gwaterHeater: null,
        television: null,
        sofa: null,
        tables: null,
        pet: null,
        parkingSpace: null,
        balcony: null,
        shortTerm: null,
        cooking: null,
        waterDispenser: null,
        managementFee: null,
        genderRestrictions: null,
      },
      searchValue: "",
      titles: [],
      rand: 1,
    };
  },
  methods: {
    filterHouses: async function () {
      let _this = this;
      var request = {};
      request.HouseID =
        isNaN(Number(_this.searchValue)) || _this.searchValue == ""
          ? -1
          : Number(_this.searchValue);
      request.Title = _this.searchValue;

      // 構建 URL，將參數作為查詢字符串附加到 URL
      const url = new URL(`${_this.baseAddress}/api/houses/details`);
      url.searchParams.append("HouseID", request.HouseID);
      url.searchParams.append("Title", request.Title);

      try {
        var response = await fetch(url, {
          method: "GET", // 使用 GET 方法
          headers: {
            "content-type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        var data = await response.json();
        _this.titles = [...new Set(data.map((obj) => obj.Title))]; // 準備不重複的標題
        _this.houses = data;
      } catch (error) {
        console.error("獲取資料時發生錯誤:", error);
      }
    },

    deleteItem: async function (item) {
      let _this = this;
      _this.Model = item;
      //alert(`delete:${item.EmployeeID}`);
      var ret = confirm("確定要刪除嗎?");
      if (ret) {
        let _this = this;
        var response = await fetch(
          `${_this.baseAddress}/api/houses/deleteCollecthouse/${item.HouseID}`,
          {
            method: "DELETE",
          }
        );
        var data = await response.text();
        alert(data);
        _this.filterHouses();
      }
    },
  },
  mounted: async function () {
    let _this = this;
    _this.filterHouses();
  },
};
var app = Vue.createApp(vueApp).mount("#app");
