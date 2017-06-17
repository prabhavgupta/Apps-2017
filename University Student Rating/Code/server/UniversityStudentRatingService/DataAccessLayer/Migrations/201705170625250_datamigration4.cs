namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class datamigration4 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Discipline", "IconPath", c => c.String(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Discipline", "IconPath");
        }
    }
}