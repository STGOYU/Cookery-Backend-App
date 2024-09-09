package com.cookerytech.service;

import com.cookerytech.config.EmailConfig;
import com.cookerytech.domain.*;
import com.cookerytech.domain.enums.OfferStatus;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.OfferDTO;
import com.cookerytech.dto.OfferDTOinItemsAndUser;
import com.cookerytech.dto.UserDTO;
import com.cookerytech.dto.request.OfferCreate;
import com.cookerytech.dto.request.OfferUpdate;
import com.cookerytech.dto.response.OfferCreateResponse;
import com.cookerytech.dto.response.UsersOfferAndItemsResponse;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.OfferItemMapper;
import com.cookerytech.mapper.OfferMapper;
import com.cookerytech.mapper.UserMapper;
import com.cookerytech.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.cookerytech.dto.response.OfferResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final UserService userService;
    private final OfferItemService offerItemService;
    private final JavaMailSender mailSender;
   private final CartItemsService cartItemsService;
    private final CurrencyService currencyService;
    private final OfferItemMapper offerItemMapper;
    private final UserMapper userMapper;

    @Autowired
    private EmailConfig emailConfig;

    public OfferService(OfferRepository offerRepository, OfferMapper offerMapper,@Lazy UserService userService, @Lazy OfferItemService offerItemService, JavaMailSender mailSender,
                        CartItemsService cartItemsService, CurrencyService currencyService, OfferItemMapper offerItemMapper, UserMapper userMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.userService = userService;
        this.offerItemService = offerItemService;
        this.mailSender = mailSender;
        this.cartItemsService = cartItemsService;
        this.currencyService = currencyService;
        this.offerItemMapper = offerItemMapper;
        this.userMapper = userMapper;
    }

     public Page<OfferDTO> findFilteredOffers(String query, Integer statusValue, LocalDate date1, LocalDate date2,
                                           Pageable pageable) {

        OfferStatus status = null;
        if (statusValue != null) {
            status = OfferStatus.fromValue(statusValue);
        } else {

            User currentUser = userService.getCurrentUser(); // Assuming you have a method to get the current user

            boolean isSalesSpecialist = false;
            boolean isSalesManager = false;

            for (Role role : currentUser.getRoles()) {
                if (role.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                    isSalesManager = true;
                    break;
                } else if (role.getType().equals(RoleType.ROLE_SALES_SPECIALIST)) {
                    isSalesSpecialist = true;
                    break;
                }
            }

            if (isSalesManager) {
                status = OfferStatus.fromValue(OfferStatus.WAITING_FOR_APPROVAL.getValue()); // Sales Managers default status
            } else if (isSalesSpecialist) {
                status = OfferStatus.fromValue(OfferStatus.CREATED.getValue()); // Sales Specialists default status
            }

        }

//        LocalDateTime dateTime1 = date1 != null ? date1.atStartOfDay() : null;
//        LocalDateTime dateTime2 = date2 != null ? date2.atStartOfDay().plusDays(1).minusSeconds(1) : null;

        /*LocalDateTime dateTime1 = date1 != null ? date1.atStartOfDay() : null;
        LocalDateTime dateTime2 = date2 != null ? date2.atTime(23, 59, 59) : null;*/

//        LocalDateTime startDateTime = date1 != null ? LocalDateTime.parse(date1 + "T00:00:00") :
//                LocalDateTime.now().minusYears(2000);
//        LocalDateTime endDateTime = date2 != null ? LocalDateTime.parse(date2 + "T23:59:59") :
//                LocalDateTime.now();
        LocalDateTime startDateTime = date1 != null ?
                LocalDateTime.of(date1.getYear(), date1.getMonth(), date1.getDayOfMonth(), 0, 0, 0) :
                LocalDateTime.now().minusYears(2000);
        LocalDateTime endDateTime = date2 != null ?
                LocalDateTime.of(date2.getYear(), date2.getMonth(), date2.getDayOfMonth(), 23, 59, 59) :
                LocalDateTime.now();

        System.out.println(startDateTime + "<- ilk tarih | ikinci tarih ->" + endDateTime);
       // System.out.println(new Offer().getSubTotal() + " <- bu sub total");
        System.out.println(new OfferDTO().getSubTotal() + " <- bu OfferDTO sub total");
        System.out.println(new Offer().getDiscount() + " <- bu discount");
        System.out.println(new OfferDTO().getDiscount() + " <- bu OfferDTO discount");
        System.out.println(new Offer().getGrandTotal() + " <- bu grand total");
        System.out.println(new OfferDTO().getGrandTotal() + " <- bu OfferDTO grand total");

        Page<Offer> filteredOffers = offerRepository.findFilteredOffers(query, status, startDateTime, endDateTime, pageable);


        return filteredOffers.map(offerMapper::offerToOfferDTO);
    }

    public OfferDTO findByIdAndUser(Long id, User user) {

        Offer offer = offerRepository.findByIdAndUser(id, user).orElseThrow(
                ()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION))
        );

        return offerMapper.offerToOfferDTO(offer);


    }

    public Page<OfferDTO> getAllOffers(String qLower, String date1, String date2, String statusLower, Pageable pageable) {

        Page<Offer> offers = null;

        if (!qLower.isEmpty() || !date1.isEmpty() || !date2.isEmpty() || !statusLower.isEmpty()) {
            offers = offerRepository.getAllOffers(qLower,date1,date2,statusLower, pageable);
        } else {
            offers = offerRepository.findAllOffersWithPage(pageable);
        }

        if(offers.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.NO_DATA_IN_DB_TABLE_MESSAGE, "Offers"));
        }
        return offers.map(offerMapper::offerToOfferDTO);
    }

    //E03  icin
    public Page<OfferDTO> getAllOffersByUser(Long userId,String date1, String date2, int status, Pageable pageable) {

        User user = userService.getById(userId);
        String newDate1 = date1+" 00:00:00.000000";
        String newDate2 = date2+" 00:00:00.000000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");//2023-09-09 15:29:21.240129
        LocalDateTime dateTime1 = LocalDateTime.parse(newDate1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(newDate2, formatter);

        OfferStatus offerStatus = OfferStatus.fromValue(status);

        Page<Offer> offers = null;

        //if (!(date1==null) || !(date2==null) || !(status==0)) {
            offers = offerRepository.getAllOffersByUser( dateTime1, dateTime2, offerStatus,user, pageable);
//        } else {
//            offers = offerRepository.findAllOffersWithPageByUser(pageable,user);
//        }

//        Page<UsersOfferAndItemsResponse> usersOfferAndItemsResponses=
//                offers.map(offer -> {
//                    return new UsersOfferAndItemsResponse(offer,offerItemService.getOfferItems(offer.getId()).size());
//                });

     return    offers.map(offerMapper::offerToOfferDTO);

    }

      public List<OfferResponse> getOffersByUserId(Long id) {
       return offerMapper.offersToOfferResponses(offerRepository.findAllByUserId(id));
    }

    public Offer getById(Long id){
        Offer offer = offerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return offer;
    }
    public OfferDTOinItemsAndUser getOfferDTOWithItemsAndUser(Long offerId) {
        Offer offer = getById(offerId);
        OfferDTOinItemsAndUser offerDTOinItemsAndUser = new OfferDTOinItemsAndUser();
        User user = userService.getCurrentUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        List<OfferItem> offerItemList = offerItemService.getOfferItems(offerId);


        offerDTOinItemsAndUser.setId(offer.getId());
        offerDTOinItemsAndUser.setCode(offer.getCode());
        offerDTOinItemsAndUser.setStatus(offer.getStatus());
        offerDTOinItemsAndUser.setUserDTO(userDTO);
        offerDTOinItemsAndUser.setOfferItemsDTO(offerItemMapper.map(offerItemList));

        return offerDTOinItemsAndUser;
    }


    //****************** offer create ********************************//
    //TODO : mail atılacak customer ve SS ye
    @Transactional
    public OfferCreateResponse makeOffer(OfferCreate offerCreate) {

        LocalDate now = LocalDate.now();
        if(offerCreate.getDeliveryDate().isBefore(now)){
            throw new BadRequestException(ErrorMessage.DELİVERY_DATE_INCORRECT_MESSAGE);
        }

        User user = userService.getCurrentUser();
        OfferCreateResponse offerCreateResponse = new OfferCreateResponse();    //return edilecek
        Offer newOffer = new Offer();                                           //DB'ye setlenecek


        //code oluşturuldu ve DB'de varmı kontrolü yapıldı
        Boolean isThereCode;
        String code;
        do{
            code = codeGenerate();
            isThereCode = codeTest(code);
        }while (isThereCode);

        //OfferId'ye ihtiyaç duyulduğu için offer create edildi
        newOffer.setCode(code);
        newOffer.setStatus(OfferStatus.CREATED);
        newOffer.setUser(user);
        newOffer.setCurrency(currencyService.getCurrency("USD"));
        newOffer.setDeliveryAt(offerCreate.getDeliveryDate());
        newOffer.setCreateAt(LocalDateTime.now());
        newOffer.setUpdateAt(LocalDateTime.now());
        Offer saveNewOffer = offerRepository.save(newOffer);

        Long offerId = saveNewOffer.getId();


         List<Cart_Items> cartItemList = cartItemsService.getCartItemsForOfferItem(user.getId());
         List<OfferItem> offferItemList = cartItemList.stream().map(cartItems -> offerItemService.offerItemsCreate(cartItems,saveNewOffer)).collect(Collectors.toList());



        //grand total setlendi
        Double grandTotal = grandTotalCalculate(offerId);
        saveNewOffer.setGrandTotal(grandTotal);
        offerRepository.save(saveNewOffer);
        //stock amount azalt
        stockAmountDecrease(offerId);

        //response için gerekli işlemler
        offerCreateResponse.setId(offerId);
        offerCreateResponse.setCode(saveNewOffer.getCode());
        offerCreateResponse.setStatus(saveNewOffer.getStatus().name());
        offerCreateResponse.setItems(offerItemMapper.map(offferItemList));
        offerCreateResponse.setUserDTO(userMapper.userToUserDTO(user));

        //mail gönderme
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getConstantEmail());
        message.setTo("meozkan07@gmail.com");
        message.setSubject("New Offer");
        message.setText(offerCreateResponse.toString());
        mailSender.send(message);

        return offerCreateResponse;
    }

    //***** unique code generate  ********
    public String codeGenerate(){

        Random random = new Random();

        List<String> codeNumeric = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","R","S","T","U","V","W","Y","Z","0","1","2","3","4","5","6","7","8","9");
        StringBuilder uniqueCode = new StringBuilder();
        for (int i=0; i<8; i++){
            int randomNumber = random.nextInt(33-0+1)+0;
            uniqueCode.append(codeNumeric.get(randomNumber));
        }
        String newCode = uniqueCode.toString();
        return newCode;
    }

    //***** Create adilen code Database'de var mı? Kontrolü yapar     ********
    public Boolean codeTest(String code){
        return offerRepository.existsByCode(code);
    }

    //***** get offer items   ****************
    public List<OfferItem> getOfferItems(Long offerId){
        return offerItemService.getOfferItems(offerId);
    }

    //*****     grand_total calculate   *****
    public Double grandTotalCalculate(Long offerId){
        List<OfferItem> offerItems = getOfferItems(offerId);
        Double grandTotal =0.0;
        for (int i=0; i<offerItems.size(); i++){
            grandTotal+=offerItems.get(i).getSubTotal()*(1- (offerItems.get(i).getDiscount()/100));
        }
        return grandTotal;
    }

    //  *****   stock amount decrease   ****

    public List<Integer> stockAmountDecrease(Long offerId){
        return offerItemService.stockAmountDecrease(offerId);
    }

    public Double numberOfOffersPerDay() {
        LocalDateTime now = LocalDateTime.now();

         // Son 24 saatlik zaman dilimi
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);

          List<Offer> offerList = offerRepository.offersPerDay(twentyFourHoursAgo);

          Double totolAmountOfOffers = 0.0;
          for (Offer offer:offerList){
              totolAmountOfOffers+=offer.getGrandTotal();
          }

          return totolAmountOfOffers;
    }


    public OfferDTO updateOffers(Long id, OfferUpdate offerUpdate) {

        Offer offer = getOffer(id);

        Set<Role> userRole = userService.getCurrentUser().getRoles();

        boolean isAdmin = false;
        boolean isSalesManager = false;
        boolean isSalesSpecialist = false;

        for (Role role : userRole) {
            if (RoleType.ROLE_ADMIN.equals(role.getType())) {
                isAdmin = true;
            } else if (RoleType.ROLE_SALES_MANAGER.equals(role.getType())) {
                isSalesManager = true;
            } else if(RoleType.ROLE_SALES_SPECIALIST.equals(role.getType())){
                isSalesSpecialist = true;
            }
        }

        if((isSalesSpecialist && (offer.getStatus().name().equals("CREATED") || offer.getStatus().name().equals("REJECTED"))) ||
                (isSalesManager && (offer.getStatus().name().equals("WAITING_FOR_APPROVAL"))) ||
                isAdmin){

            offer.setDiscount(offerUpdate.getDiscount());
            offer.setStatus(offerUpdate.getStatus());
            offer.setCurrency(currencyService.getCurrencyById(offerUpdate.getCurrencyId()));

        }else {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        Offer updateOffer = offerRepository.save(offer);
        //List<OfferItem> offerItemList = offerItemService.getOfferItems(id);


        OfferDTO offerDTO = offerMapper.offerToOfferDTO(updateOffer);

        return offerDTO;
        //List<OfferItemDTO> offerItemDTOs = offerItemMapper.map(offerItemList);

//        UpdateOfferResponse offerResponse = new UpdateOfferResponse();
//        offerResponse.setOfferDTO(offerDTO);
//        offerResponse.setOfferItemDTOList(offerItemDTOs);

        // return offerResponse;


    }


    public Offer getOffer(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id))
        );
        return offer;
    }


    public List<Offer> getAllOffersBetweenDate(LocalDateTime dateTime1, LocalDateTime dateTime2) {
//        Pageable pageable = PageRequest.of(0, 20, Sort.by("createAt").descending());
        List<Offer> offers = offerRepository.getAllOffersBetweenDate( dateTime1, dateTime2);
        return offers;
    }
    public Boolean existsByUser(User user) {
        Boolean exists = offerRepository.existsByUser(user);
        return exists;
    }
}
