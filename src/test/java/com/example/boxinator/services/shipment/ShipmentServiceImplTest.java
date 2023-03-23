package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentGetDto;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;

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
    void createShipmentTest() {

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

    ShipmentStatus buildStatus1(Status status) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setTs(new Timestamp(System.currentTimeMillis()));
     //   shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }
    @Test
    void testGetShipmentsFilteredByStatus() {

        Long accountId = 1L;
        //Local
        String from = "2020-03-20";
        String to = "2023-03-22";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(from, formatter);
        Instant instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Date startDate = Date.from(instant);

        LocalDate localDateTo = LocalDate.parse(to, formatter);
        Instant instantTo = localDateTo.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        Date endDate = Date.from(instantTo);


        List<Status> statuses = Arrays.asList(Status.CREATED, Status.COMPLETED);


        Shipment shipment1 = Mockito.mock(Shipment.class);
        Mockito.when(shipment1.getId()).thenReturn(1L);
        Mockito.when(shipment1.getStatuses()).thenReturn(List.of(buildStatus1(Status.CREATED)));

        Shipment shipment2 = Mockito.mock(Shipment.class);
        Mockito.when(shipment2.getId()).thenReturn(2L);
        Mockito.when(shipment2.getStatuses()).thenReturn(List.of(buildStatus1(Status.COMPLETED)));

        List<Shipment> expectedShipments = Arrays.asList(shipment1, shipment2);

        Mockito.when(shipmentRepository.findAllByAccountAndDateBetween(any(), any(), any(), any())).thenReturn(expectedShipments);

        List<Shipment> actualShipments = service.getShipmentsFiltered(accountId, startDate, endDate, statuses);

        System.out.println("Actual shipments " + actualShipments);
        System.out.println("Expected shipments: " + expectedShipments);
        assertEquals(expectedShipments, actualShipments);

        Mockito.verify(shipmentRepository).findAllByAccountAndDateBetween(accountId, startDate, endDate, statuses.stream().map(Enum::ordinal).toList());


    }

    @Test
    void getById() {
        Long accountId = 1L;

        Shipment expectedShipment = buildShipment();

        Mockito.when(shipmentRepository.findById(accountId)).thenReturn(Optional.of(expectedShipment));

        service.getById(accountId);

        Mockito.verify(shipmentRepository, Mockito.times(1)).findById(accountId);
    }


    @Test
    void getAll() {
        List<Shipment> expectedShipments = new ArrayList<>();
        expectedShipments.add(buildShipment());

        Mockito.when(shipmentRepository.findAll()).thenReturn(expectedShipments);

        List<Shipment> actualShipments = service.getAll();

        assertEquals(expectedShipments, actualShipments);
    }

    @Test
    void deleteById() {
        long shipmentId = 1L;

        Shipment expectedShipment = buildShipment();

        Mockito.when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(expectedShipment));

        service.deleteById(shipmentId);

        Mockito.verify(shipmentRepository, Mockito.times(1)).deleteById(shipmentId);
    }

    private ShipmentStatus buildStatus(Status status, Shipment shipment) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setStatus(status);
        shipmentStatus.setTs(new Date());
        shipmentStatus.setShipment(shipment);
        return shipmentStatus;
    }
    @Test
    void updateShipmentStatus() {

        Long shipmentId = 1L;
        Status status = Status.COMPLETED;
        Shipment shipment = buildShipment();
        Mockito.when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));


        Mockito.when(shipmentStatusRepository.save(any())).thenAnswer(it -> it.getArgument(0));

        Shipment result = service.updateShipmentStatus(shipmentId, status);

        assertEquals(result,shipment);
        Mockito.verify(shipmentRepository).findById(shipmentId);


        ShipmentStatus expectedShipmentStatus = buildStatus(status, shipment);
        Mockito.verify(shipmentStatusRepository, Mockito.times(1)).save(any());

    }

    @Test
    void update() {

        long shipmentId = 1L;

        ShipmentPostDto inputDto = new ShipmentPostDto();
        inputDto.setRecipient("Updated Recipient");
        inputDto.setBoxColor("#FFFFF");
        inputDto.setBoxTierId(1L);
        inputDto.setCountryId(1L);

        Shipment expectedShipment = buildShipment();
        Mockito.when(expectedShipment.getRecipient()).thenReturn("Updated Recipient");
        Mockito.when(expectedShipment.getBoxColor()).thenReturn("#FFFFF");


        Mockito.when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(expectedShipment));
        Mockito.when(shipmentMapper.toShipment(inputDto)).thenReturn(expectedShipment);

        Mockito.when(shipmentRepository.save(any())).thenAnswer(it -> it.getArgument(0));

        Shipment actualShipment = service.update(shipmentId, inputDto);

        assertEquals(expectedShipment, actualShipment);
        assertEquals("Updated Recipient", expectedShipment.getRecipient());

        Mockito.verify(shipmentRepository).save(expectedShipment);

    }
}