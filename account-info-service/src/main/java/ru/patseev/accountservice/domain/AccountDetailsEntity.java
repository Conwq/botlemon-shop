package ru.patseev.accountservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(schema = "botlemon", name = "account_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;
	private Timestamp lastLoginDate;
	private BigDecimal totalPurchaseAmount;
	private Integer bonusPoints;
	private Integer userId;
}