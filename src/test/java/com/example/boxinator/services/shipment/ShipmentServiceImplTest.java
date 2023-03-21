package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.country.CountryTier;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.account.AccountRepository;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.repositories.shipment.ShipmentStatusRepository;
import com.example.boxinator.services.acoount.AccountService;
import com.example.boxinator.services.box.BoxService;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.fee.FeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ShipmentServiceImplTest {

    private ShipmentServiceImpl service;
    private ShipmentRepository shipmentRepository;
    private ShipmentStatusRepository shipmentStatusRepository;
    private AccountRepository accountRepository;


    private CountryService countryService;
    private FeeService feeService;
    private BoxService  boxService;
    private AccountService accountService;
    private ShipmentMapper shipmentMapper;

    @BeforeEach
    void setUp() {
        shipmentRepository = Mockito.mock(ShipmentRepository.class);
        shipmentStatusRepository = Mockito.mock(ShipmentStatusRepository.class);
        shipmentMapper = Mockito.mock(ShipmentMapper.class);
        accountRepository = Mockito.mock(AccountRepository.class);
        countryService = Mockito.mock(CountryService.class);
        feeService = Mockito.mock(FeeService.class);
        boxService = Mockito.mock(BoxService.class);
        accountService = Mockito.mock(AccountService.class);


        service = new ShipmentServiceImpl(
                feeService,
                shipmentRepository,
                shipmentStatusRepository,
                shipmentMapper,
                countryService,
                accountService,
                boxService,
                accountRepository

        );
    }

     private Shipment buildShipment() {
        CountryTier countryTier = Mockito.mock(CountryTier.class);
        Mockito.when(countryTier.getId()).thenReturn(1L);
        Mockito.when(countryTier.getName()).thenReturn("Tier 1");
        Mockito.when(countryTier.getShippingMultiplier()).thenReturn(1.0F);

        BoxTier boxTier = Mockito.mock(BoxTier.class);
        Mockito.when(boxTier.getId()).thenReturn(1L);
        Mockito.when(boxTier.getName()).thenReturn("Deluxe");
        Mockito.when(boxTier.getWeight()).thenReturn(1F);

        Country country = Mockito.mock(Country.class);
        Mockito.when(country.getName()).thenReturn("Test Country");
        Mockito.when(country.getId()).thenReturn(1L);
        Mockito.when(country.getTier()).thenReturn(countryTier);

        Fee fee = Mockito.mock(Fee.class);
        Mockito.when(fee.getAmount()).thenReturn(200F);
        Mockito.when(fee.getName()).thenReturn("Base Shipment Cost");
        Mockito.when(fee.getId()).thenReturn(1L);

        //   Status status = Mockito.mock(Status.class);
        //    Mockito.when(status)


         ShipmentStatus shipmentStatus = Mockito.mock(ShipmentStatus.class);
         Mockito.when(shipmentStatus.getStatus()).thenReturn(Status.COMPLETED);



        Shipment shipment = Mockito.mock(Shipment.class);
        Mockito.when(shipment.getId()).thenReturn(1L);
        Mockito.when(shipment.getRecipient()).thenReturn("Test Customer");
        Mockito.when(shipment.getCost()).thenReturn(200F);
        Mockito.when(shipment.getBoxColor()).thenReturn("#FFFFFF");
        Mockito.when(shipment.getCountry()).thenReturn(country);
        Mockito.when(shipment.getBoxTier()).thenReturn(boxTier);

        Mockito.when(shipment.getStatuses()).thenReturn(List.of(shipmentStatus));
        Mockito.when(feeService.calculateShipmentCost(1L,1L)).thenReturn(fee);
        Mockito.when(shipmentMapper.toShipment(any())).thenReturn(shipment);

        return shipment;
    }

    @Test
    void create() {

        ShipmentPostDto shipmentPostDto  = new ShipmentPostDto();
        shipmentPostDto.setBoxTierId(1L);
        shipmentPostDto.setRecipient("Test Customer");
        shipmentPostDto.setBoxColor("#FFFFFF");
        shipmentPostDto.setCountryId(1L);

        CountryTier countryTier = Mockito.mock(CountryTier.class);
        Mockito.when(countryTier.getId()).thenReturn(1L);
        Mockito.when(countryTier.getName()).thenReturn("Tier 1");
        Mockito.when(countryTier.getShippingMultiplier()).thenReturn(1.0F);

        BoxTier boxTier = Mockito.mock(BoxTier.class);
        Mockito.when(boxTier.getId()).thenReturn(1L);
        Mockito.when(boxTier.getName()).thenReturn("Deluxe");
        Mockito.when(boxTier.getWeight()).thenReturn(1F);

        Country country = Mockito.mock(Country.class);
        Mockito.when(country.getName()).thenReturn("Test Country");
        Mockito.when(country.getId()).thenReturn(1L);
        Mockito.when(country.getTier()).thenReturn(countryTier);

        Fee fee = Mockito.mock(Fee.class);
        Mockito.when(fee.getAmount()).thenReturn(200F);
        Mockito.when(fee.getName()).thenReturn("Base Shipment Cost");
        Mockito.when(fee.getId()).thenReturn(1L);


        Shipment shipment = Mockito.mock(Shipment.class);
        Mockito.when(shipment.getId()).thenReturn(1L);
        Mockito.when(shipment.getRecipient()).thenReturn("Test Customer");
        Mockito.when(shipment.getCost()).thenReturn(200F);
        Mockito.when(shipment.getBoxColor()).thenReturn("#FFFFFF");
        Mockito.when(shipment.getCountry()).thenReturn(country);
        Mockito.when(shipment.getBoxTier()).thenReturn(boxTier);

        Mockito.when(feeService.calculateShipmentCost(1L,1L)).thenReturn(fee);
        Mockito.when(shipmentMapper.toShipment(any())).thenReturn(shipment);

        Shipment createdShipment = service.createNewShipment(1L, shipmentPostDto);


        assertNotNull(createdShipment);
        assertEquals(shipment.getRecipient(), createdShipment.getRecipient());
        assertEquals(shipment.getCountry(), createdShipment.getCountry());
        assertEquals(shipment.getCost(), createdShipment.getCost());

    }

    ShipmentStatus buildStatus(Status status) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setTs(new Timestamp(System.currentTimeMillis()));
     //   shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }
    @Test
    void testGetShipmentsFilteredByStatus() {

        Long accountId = 1L;
        Date from = null;
        Date to = null;
        List<Status> statuses = Arrays.asList(Status.CREATED, Status.COMPLETED);



        Shipment shipment1 = Mockito.mock(Shipment.class);
        Mockito.when(shipment1.getId()).thenReturn(1L);
        Mockito.when(shipment1.getStatuses()).thenReturn(List.of(buildStatus(Status.CREATED)));

        Shipment shipment2 = Mockito.mock(Shipment.class);
        Mockito.when(shipment2.getId()).thenReturn(2L);
        Mockito.when(shipment2.getStatuses()).thenReturn(List.of(buildStatus(Status.COMPLETED)));

        List<Shipment> expectedShipments = Arrays.asList(shipment1, shipment2);

        Mockito.when(shipmentRepository.findAllByAccountAndDateBetween(any(), any(), any(), any())).thenReturn(expectedShipments);

        List<Shipment> actualShipments = service.getShipmentsFiltered(accountId, from, to, statuses);

        System.out.println(actualShipments);
        assertEquals(expectedShipments, actualShipments);

        Mockito.verify(shipmentRepository).findAllByAccountAndDateBetween(accountId, from, to, statuses.stream().map(Enum::ordinal).toList());


    }

    @Test
    void getById() {
    }

    @Test
    void getAccountShipments() {
    }

    @Test
    void getByStatus() {
    }

    @Test
    void calculateShipmentCost() {
    }

    @Test
    void createNewShipment() {
    }

    @Test
    void getAll() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateShipmentStatus() {
    }

    @Test
    void update() {
    }
}