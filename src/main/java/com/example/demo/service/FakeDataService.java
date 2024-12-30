package com.example.demo.service;


import com.example.demo.helper.SearchHelper;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.MessageBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.HouseImageRepository;
import com.example.demo.repository.HouseRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.andreinc.mockneat.MockNeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
public class FakeDataService {

    @Autowired
    private SearchHelper searchHelp;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private HouseRepository houseRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private HouseImageRepository houseImageRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final MockNeat mock = MockNeat.threadLocal();
    private final Random random = new Random();

    private final String[] lastNames = {
            "陳", "林", "王", "張", "劉", "李", "黃", "吳", "蔡", "楊",
            "許", "鄭", "謝", "洪", "郭", "邱", "曾", "廖", "賴", "周",
            "徐", "蘇", "葉", "莊", "呂", "江", "何", "蕭", "羅", "高",
            "潘", "簡", "朱", "鍾", "彭", "游", "詹", "胡", "施", "沈",
            "余", "盧", "葉", "卓", "方", "宋", "唐", "涂", "戴", "石",
            "龍", "陸", "溫", "高", "田", "馬", "白", "尤", "龔", "傅",
            "柴", "韓", "覃", "歐", "黎", "程", "童", "尹", "戴", "甘",
            "范", "丁", "藍", "柯", "喬", "邢", "尹", "戚", "鮑", "盛",
            "卓", "謝", "嵇", "焦", "杭", "岑", "段", "童", "魏", "顏",
            "倪", "湯", "滕", "殷", "羅", "畢", "韋", "田", "秦", "雷"
    };

    private final String[] firstNames = {
            "嘉", "偉", "志", "婷", "宏", "淑", "秀", "明", "建", "美",
            "國", "榮", "佳", "君", "惠", "玉", "佩", "怡", "家", "欣",
            "清", "雄", "育", "靜", "文", "成", "芳", "俊", "宗", "瑞",
            "宇", "昱", "冠", "姿", "豪", "承", "振", "惠", "珊", "靜",
            "茜", "倩", "琦", "夢", "涵", "詩", "瑩", "敏", "霖", "睿",
            "杰", "峰", "浩", "洋", "威", "智", "豪", "哲", "勛", "倫",
            "庭", "芷", "函", "翊", "昕", "皓", "佑", "婷", "珮", "筠",
            "瑜", "婷", "樂", "彥", "妤", "筱", "茹", "穎", "榕", "靖",
            "芹", "薇", "純", "靜", "凡", "芷", "馨", "莉", "韻", "瑜",
            "姿", "婕", "媛", "希", "璇", "翎", "靜", "桐", "雅", "琪",
            "瑋", "琳", "慧", "瀅", "恩", "憶", "念", "俞", "儀", "諾",
            "菡", "蘭", "茵", "璉", "紗", "瑄", "淇", "鈴", "甯", "澤",
            "賢", "諺", "靖", "璋", "渝", "涵", "翊", "宸", "凡", "晉",
            "嫣", "曦", "暉", "駿", "耀", "庭", "聖", "譽", "喬", "琪",
            "寧", "昭", "伊", "霜", "昕", "翔", "楷", "勳", "傑", "峻",
            "皓", "琪", "筱", "璿", "芊", "翊", "筠", "榛", "嵐", "甯",
            "澄", "渝", "謙", "靜", "恬", "岑", "靄", "茗", "蓁", "翎"
    };

    private final String[] houseTitles = {
            "豪華裝潢，溫馨舒適",
            "超值單人套房，拎包入住",
            "採光極佳，溫馨居家",
            "獨立空間，全新裝修",
            "高樓層景觀房，視野開闊",
            "精緻小資生活，輕鬆入住",
            "設備齊全，現代簡約風格",
            "高性價比，學區房熱租中",
            "全新傢俱，租金優惠",
            "家庭友好型，設備完善",
            "全新翻修，理想的家庭居所",
            "大空間客廳，適合家人聚會",
            "安靜社區，生活便利",
            "精緻單人房，適合獨居生活",
            "簡約小套房，輕鬆生活",
            "超值單人空間，隨時入住",
            "靜謐工作室，理想的創意空間",
            "小而美，完美的個人居所",
            "有質感的現代風單人房",
            "溫馨小套房，讓人一見傾心",
            "生活機能佳，單人公寓出租",
            "一個人的幸福角落，租金優惠",
            "靠近學校，適合學生族",
            "豪華設計，享受高品質生活",
            "精選裝潢，品味生活的最佳選擇",
            "頂級配置，舒適享受",
            "獨特設計，品味非凡",
            "採光無敵，打造明亮居家空間",
            "夢幻公寓，完美的生活體驗",
            "精美裝修，細節滿分",
            "時尚公寓，適合品味人士",
            "讓生活更美好的理想居所",
            "極致舒適，豪華單位出租",
            "超划算！租金含管理費",
            "價格親民，值得一看",
            "平價出租，物超所值",
            "單身族首選，租金超低",
            "超高CP值，絕對不容錯過",
            "小資族租屋首選",
            "超值推薦，租金含水電費",
            "經濟實惠，生活無壓力",
            "預算有限？這裡最適合",
            "限時優惠，租金大減價"
    };

    private final String[][] rentPriceRanges = {
            {"台北市", "15000", "30000"}, // 最低價格, 最高價格
            {"新北市", "12000", "25000"},
            {"桃園市", "10000", "20000"},
            {"台中市", "9000", "18000"},
            {"台南市", "8000", "15000"},
            {"高雄市", "8000", "16000"},
            {"基隆市", "8500", "14000"},
            {"新竹市", "14000", "22000"},
            {"新竹縣", "13000", "21000"},
            {"苗栗縣", "7000", "12000"},
            {"彰化縣", "7500", "13000"},
            {"南投縣", "7000", "11000"},
            {"雲林縣", "6500", "10000"},
            {"嘉義市", "8500", "14000"},
            {"嘉義縣", "7500", "13000"},
            {"屏東縣", "8000", "14000"},
            {"宜蘭縣", "8500", "16000"},
            {"花蓮縣", "9000", "17000"},
            {"台東縣", "8500", "14000"},
            {"澎湖縣", "7500", "12000"},
            {"金門縣", "8000", "13000"},
            {"連江縣", "7000", "11000"}
    };

    private final String[] rentDescriptions = {
            "台灣租屋市場多樣化，涵蓋從學生宿舍到高端公寓的各類房型，適合不同需求。",
            "租屋時選擇鄰近公車站或捷運站的地點，能大幅減少通勤時間。",
            "隨著工作模式改變，越來越多人選擇具備家庭辦公空間的租屋選項。",
            "短期租賃成為旅居族與數位遊牧族的熱門選擇，靈活性高但價格稍高。",
            "部分租屋平台提供實價登錄資訊，讓租金透明化，方便租客議價。",
            "新興租屋平台加入了線上簽約和付款功能，提升交易的便利性與安全性。",
            "隨著物價上漲，合租逐漸成為年輕族群降低租金壓力的有效方式。",
            "許多房東開始提供含傢俱和家電的出租方案，吸引短期租賃者。",
            "選擇屋況良好的房子雖然租金稍高，但能減少後續的維修困擾。",
            "房屋租賃法規日趨完善，對租客和房東都提供了更好的保障。",
            "學生族群偏好鄰近學校且租金實惠的小套房或分租雅房。",
            "家庭租屋需求多集中在空間寬敞的整層出租，並偏好鄰近學區的地段。",
            "上班族通常選擇交通樞紐附近的租屋，儘管租金較高，但通勤便利。",
            "隨著共享經濟的興起，合租社區提供更多共享設施，例如健身房和廚房。",
            "租屋市場旺季集中在每年的6月至9月，學生與新鮮人大量進入市場。",
            "租屋合約的長度通常為一年，短租需求則以服務公寓或民宿為主。",
            "交通便利性和周邊生活機能是租屋選址時的重要考量因素。",
            "選擇屋主直租的物件可以節省仲介費，但需注意合約條款。",
            "在地震多發的地區，選擇較新且符合建築法規的房子尤為重要。",
            "房屋隔音效果對租客的生活品質影響很大，建議在簽約前實地查看。",
            "台灣的租屋市場逐漸數位化，虛擬實境看房技術正變得越來越普及。",
            "租屋時應特別注意隱性費用，如停車費、網路費和管理費是否另計。",
            "許多出租物件允許養寵物，但應提前確認相關限制，避免租後爭議。",
            "不同地區的租金水平差異大，租客應根據工作地點和生活需求綜合考量。",
            "夜市和商圈附近的房屋通常租金稍高，但生活方便且選擇多樣化。",
            "選擇社區型租屋能享受更多設施，如保全、垃圾集中處理和健身中心。",
            "鄉村地區的租金雖然低廉，但需考量通勤時間和交通成本。",
            "台灣的海景房屋出租愈來愈受到追捧，但租金也相應較高。",
            "為節省搬遷成本，許多租客偏好附帶基本家具的物件。",
            "租屋市場的淡季通常出現在農曆春節期間，這時候租金可能較有彈性。"
    };

    private final String[] houseTypes = {
            "透天", "獨立套房", "分租套房", "雅房",
    };

    public void userFakeData() throws IOException {

        log.info(passwordEncoder.encode("Pasword123"));
        for (int i = 0; i < 7046; i++) {
            UserTableBean user = new UserTableBean();
            //user.setUserId((long) i);
            user.setName(getRandomName());
            user.setEmail(mock.emails().val());
//            user.setPassword(passwordEncoder.encode(getRandomPassword()));
            user.setPassword(passwordEncoder.encode("Password123"));
            user.setPhone(mock.regex("09\\d{8}").val());
            user.setPicture(getMemberJpg());
            user.setCreateTime(getRandomLocalDateTime(
                    LocalDate.of(2024,1,1),
                    LocalDate.of(2024,12,31)));
            user.setGender((byte)random.nextInt(2));
            user.setCoupon((byte)3);
            user.setStatus((byte)random.nextInt(3));
            userRepo.save(user);
        }
    }

    public void messageFakeData(){
        for (int i = 1133; i < 7046; i++) {
            MessageBean message = new MessageBean();
            //UserTableBean user = getRandomUser(7046);
            UserTableBean user = getUser(i);
            message.setSenderId((long)3);
            message.setReceiverId(user.getUserId());
            message.setMessage(String.join("" ,
                    "歡迎光臨 ",user.getName()," 來到我們網站,有送3張折價卷到你的帳戶"));

            //TODO 要塞照片
            messageRepo.save(message);
        }
    }

    public void houseFakeData() throws InterruptedException {
        for (int i = 1; i < 7046; i++) {
            HouseTableBean house = new HouseTableBean();
            UserTableBean user = getRandomUser(7046);
            List<String> lists = openfileRead("C:\\Users\\User\\Desktop\\Rent_index\\address.csv");
            double[] latlng = placeToLagLngForRegister(lists.get(i));

            house.setUser(user);
            house.setTitle(getRandomHouseTitle());
            house.setDescription(getRandomHouseDescription());
            house.setSize(8 + random.nextInt(17));
            house.setAddress(lists.get(i));
            house.setLat(latlng[0]);
            house.setLng(latlng[1]);
            house.setPrice(getRandomHousePrice(house.getAddress().substring(0,3)));
            house.setRoom((byte)(1+random.nextInt(3)));
            house.setBathroom((byte)(1+random.nextInt(3)));
            house.setLivingroom((byte)(1+random.nextInt(2)));
            house.setKitchen((byte)(1+random.nextInt(2)));
            house.setFloor((byte)(1+random.nextInt(4)));
            house.setAtticAddition(random.nextInt(2) != 0);
            house.setHouseType(houseTypes[random.nextInt(houseTypes.length)]);
            house.setStatus((byte)random.nextInt(3));
            house.setClickCount(random.nextInt(2000));
            houseRepo.save(house);
            Thread.sleep(100);
        }
    }

    public void imageFakeData() throws IOException {

        UserTableBean user = getRandomUser(7046);
        for(int i = 801 ; i < 7046; i++) {

            HouseTableBean house = getHouse(i);

            if ( i % 3 == 0){
                user = getRandomUser(7046);
            }

            int type = 1+random.nextInt(8);
            for(int j = 1 ; j < 3 ; j++ ){
                HouseImageTableBean houseImage = new HouseImageTableBean();
                houseImage.setHouse(house);
                houseImage.setUser(user);
                houseImage.setImages(getRandomHouseJpg(type,j));
                houseImageRepo.save(houseImage);
            }
        }
    }

    private void bookingTimeSlotFakeData() throws IOException {}



    private String getRandomName() {
//        Random random = new Random();
        String lastName = lastNames[random.nextInt(lastNames.length)];
        int firstNameLength = 2;
        StringBuilder firstName = new StringBuilder();
        for (int i = 0; i < firstNameLength; i++) {
            firstName.append(firstNames[random.nextInt(firstNames.length)]);
        }
        return lastName + firstName;
    }
    private LocalDateTime getRandomLocalDateTime(LocalDate dateStart, LocalDate dateEnd) {
        LocalDate randomDate = getRandomDate(dateStart, dateEnd);
        LocalTime randomTime = getRandomTime();
        return LocalDateTime.of(randomDate,randomTime);
    }
    private LocalTime getRandomTime(){
        int randomHousr = ThreadLocalRandom.current().nextInt(0,24);
        int randomMinute = ThreadLocalRandom.current().nextInt(0,60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0,60);
        return LocalTime.of(randomHousr, randomMinute, randomSecond);
    }

    private LocalDate getRandomDate(LocalDate start , LocalDate end){
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay+1);
        return LocalDate.ofEpochDay(randomDay);
    }
    private String getRandomPassword(){
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String password = mock.from(charset.split(""))
                .list(12) // 指定密碼長度，例如 12 位
                .val()
                .stream()
                .reduce("", String::concat); // 合併字符
        return password;
    }

    private byte[] getMemberJpg() throws IOException {
        String picPath = "src/main/resources/static/img/Member.jpeg";
        File file = new File(picPath);

        return Files.readAllBytes(file.toPath());
    }
    private UserTableBean getUser(long id){
        UserTableBean user = new UserTableBean();
        Optional<UserTableBean> op = userRepo.findById(id);
        if (op.isPresent()){
            user = op.get();
        }
        return user;
    }

    private UserTableBean getRandomUser(int max){
//        Random random = new Random();
        int randomValue = random.nextInt(max);
        UserTableBean user = new UserTableBean();
        Optional<UserTableBean> op = userRepo.findById((long)randomValue);
        if (op.isPresent()){
            user = op.get();
        }
        return user;
    }
    private String getRandomHouseTitle(){
//        Random random = new Random();
        String houseTitle = houseTitles[random.nextInt(houseTitles.length)];
        return houseTitle;
    }
    private String getRandomHouseDescription(){
        String houseDescription = rentDescriptions[random.nextInt(rentDescriptions.length)];
        return houseDescription;

    }
    private int getRandomHousePrice(String address){
        int minPrice=0;
        int gapPrice=0;
        for(int i = 0 ; i < rentPriceRanges.length ; i++){
            if (rentPriceRanges[i][0].equals(address)){
                minPrice = Integer.valueOf(rentPriceRanges[i][1]);
                gapPrice = Integer.valueOf(rentPriceRanges[i][2]) - minPrice;
                break;
            }
        }
        return minPrice + random.nextInt(gapPrice);
    }

    private HouseTableBean getRandomHouse(int max){
//        Random random = new Random();
        int randomValue = random.nextInt(max);
        HouseTableBean house = new HouseTableBean();
        Optional<HouseTableBean> op = houseRepo.findById((long)randomValue);
        if (op.isPresent()){
            house = op.get();
        }
        return house;
    }
    private HouseTableBean getHouse(int id){
        HouseTableBean house = new HouseTableBean();
        Optional<HouseTableBean> op = houseRepo.findById((long)id);
        if (op.isPresent()){
            house = op.get();
        }
        return house;
    }
    private byte[] getRandomHouseJpg(int type , int j) throws IOException {

        String picPath = "src/main/resources/static/img/house" + type + j + ".jpeg";
        File file = new File(picPath);

        return Files.readAllBytes(file.toPath());
    }

    private List<String> openfileRead(String filePath){

        List<String> lists = new ArrayList<String>();

        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis,"MS950");
             BufferedReader br = new BufferedReader(isr);)
        {
            String line;
            //line = br.readLine();
            while((line =br.readLine()) != null) {
                lists.add(line);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return lists;
    }
    public double[] placeToLagLngForRegister(String registerAddress) {
        double[] location = new double[2];

        try {
            JSONObject json = searchHelp.fetchGeocodeFromAPI(registerAddress);
            searchHelp.parseLatLng(json).ifPresent(latlng ->{
                location[0] = latlng[0];
                location[1] = latlng[1];
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;

    }

}
