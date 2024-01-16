package ru.patseev.accountservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(schema = "botlemon", name = "account_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsEntity {
	@Id
	@Column(name = "account_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;
	@Column(name = "last_login_date")
	private Timestamp lastLoginDate;
	@Column(name = "total_purchase_amount")
	private BigDecimal totalPurchaseAmount;
	@Column(name = "bonus_points")
	private Integer bonusPoints;
	@Column(name = "user_id")
	private Integer userId;
}