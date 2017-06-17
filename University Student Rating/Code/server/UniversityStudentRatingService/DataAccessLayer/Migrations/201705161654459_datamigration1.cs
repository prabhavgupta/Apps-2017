namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class datamigration1 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.UserProfile", "CityId", "dbo.City");
            DropForeignKey("dbo.UserProfile", "CountryId", "dbo.Country");
            DropForeignKey("dbo.UserProfile", "UniversityId", "dbo.University");
            DropForeignKey("dbo.UserProfile", "GenderTypeId", "dbo.GenderType");
            DropForeignKey("dbo.UserProfile", "StatusId", "dbo.Status");
            DropIndex("dbo.UserProfile", new[] { "GenderTypeId" });
            DropIndex("dbo.UserProfile", new[] { "CountryId" });
            DropIndex("dbo.UserProfile", new[] { "CityId" });
            DropIndex("dbo.UserProfile", new[] { "UniversityId" });
            DropIndex("dbo.UserProfile", new[] { "StatusId" });
            AlterColumn("dbo.UserProfile", "GenderTypeId", c => c.Int(nullable: false));
            AlterColumn("dbo.UserProfile", "CountryId", c => c.Int(nullable: false));
            AlterColumn("dbo.UserProfile", "CityId", c => c.Int(nullable: false));
            AlterColumn("dbo.UserProfile", "UniversityId", c => c.Int(nullable: false));
            AlterColumn("dbo.UserProfile", "StatusId", c => c.Int(nullable: false));
            CreateIndex("dbo.UserProfile", "GenderTypeId");
            CreateIndex("dbo.UserProfile", "CountryId");
            CreateIndex("dbo.UserProfile", "CityId");
            CreateIndex("dbo.UserProfile", "UniversityId");
            CreateIndex("dbo.UserProfile", "StatusId");
            AddForeignKey("dbo.UserProfile", "CityId", "dbo.City", "Id", cascadeDelete: true);
            AddForeignKey("dbo.UserProfile", "CountryId", "dbo.Country", "Id", cascadeDelete: true);
            AddForeignKey("dbo.UserProfile", "UniversityId", "dbo.University", "Id", cascadeDelete: true);
            AddForeignKey("dbo.UserProfile", "GenderTypeId", "dbo.GenderType", "Id", cascadeDelete: true);
            AddForeignKey("dbo.UserProfile", "StatusId", "dbo.Status", "Id", cascadeDelete: true);
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.UserProfile", "StatusId", "dbo.Status");
            DropForeignKey("dbo.UserProfile", "GenderTypeId", "dbo.GenderType");
            DropForeignKey("dbo.UserProfile", "UniversityId", "dbo.University");
            DropForeignKey("dbo.UserProfile", "CountryId", "dbo.Country");
            DropForeignKey("dbo.UserProfile", "CityId", "dbo.City");
            DropIndex("dbo.UserProfile", new[] { "StatusId" });
            DropIndex("dbo.UserProfile", new[] { "UniversityId" });
            DropIndex("dbo.UserProfile", new[] { "CityId" });
            DropIndex("dbo.UserProfile", new[] { "CountryId" });
            DropIndex("dbo.UserProfile", new[] { "GenderTypeId" });
            AlterColumn("dbo.UserProfile", "StatusId", c => c.Int());
            AlterColumn("dbo.UserProfile", "UniversityId", c => c.Int());
            AlterColumn("dbo.UserProfile", "CityId", c => c.Int());
            AlterColumn("dbo.UserProfile", "CountryId", c => c.Int());
            AlterColumn("dbo.UserProfile", "GenderTypeId", c => c.Int());
            CreateIndex("dbo.UserProfile", "StatusId");
            CreateIndex("dbo.UserProfile", "UniversityId");
            CreateIndex("dbo.UserProfile", "CityId");
            CreateIndex("dbo.UserProfile", "CountryId");
            CreateIndex("dbo.UserProfile", "GenderTypeId");
            AddForeignKey("dbo.UserProfile", "StatusId", "dbo.Status", "Id");
            AddForeignKey("dbo.UserProfile", "GenderTypeId", "dbo.GenderType", "Id");
            AddForeignKey("dbo.UserProfile", "UniversityId", "dbo.University", "Id");
            AddForeignKey("dbo.UserProfile", "CountryId", "dbo.Country", "Id");
            AddForeignKey("dbo.UserProfile", "CityId", "dbo.City", "Id");
        }
    }
}
